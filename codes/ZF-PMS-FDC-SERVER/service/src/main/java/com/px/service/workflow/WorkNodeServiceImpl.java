package com.px.service.workflow;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.px.common.BaseEntity;
import com.px.dto.HandlerPermissionDTO;
import com.px.dto.WorkNodeDTO;
import com.px.entity.WorkFlow;
import com.px.entity.WorkNode;
import com.px.entity.WorkNodeTask;
import com.px.enums.MainBusinessEnum;
import com.px.enums.ProductionEnum;
import com.px.enums.ReportStatus;
import com.px.enums.workflow.NodeTaskVersion;
import com.px.mapper.WorkFlowMapper;
import com.px.mapper.WorkNodeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.vo.NameCodeVO;
import com.px.enums.workflow.WorkflowNodeEnum;
import com.google.common.cache.Cache;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.px.enums.workflow.WorkflowNodeEnum.*;

/**
 * <p>
 * 工作流节点 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
@Service
public class WorkNodeServiceImpl extends ServiceImpl<WorkNodeMapper, WorkNode> implements IWorkNodeService {


    @Autowired
    private IWorkNodeTaskService workNodeTaskService;

    @Autowired
    private WorkNodeMapper workNodeMapper;

    @Autowired
    private WorkFlowMapper workFlowMapper;

    @Autowired
    @Qualifier("workflowCache")
    private Cache<Long, List<WorkNodeDTO>> workflowCache;


    @Override
    @SuppressWarnings("unchecked")
    public Page<WorkNode> selectPage(Page page, WorkNode workNode) {
        return this.page(page, new QueryWrapper<WorkNode>(workNode));
    }


    @Override
    public WorkNode detail(Long id) {
        return this.getById(id);
    }

    @Transactional
    @Override
    public Boolean add(WorkNodeDTO workNode) {
        Long priviousNodeId = workNode.getPriviousNodeId();
        if (priviousNodeId != null) {
            WorkNode ex = this.getOne(new LambdaQueryWrapper<WorkNode>().eq(WorkNode::getPriviousNodeId, priviousNodeId));
            if (ex != null) {
                Long nodeId = saveNodeAndTask(workNode);
                return this.update(new LambdaUpdateWrapper<WorkNode>().set(WorkNode::getPriviousNodeId, nodeId).eq(BaseEntity::getId, ex.getId()));
            }
        } else {
            WorkNode ex = this.getOne(new LambdaQueryWrapper<WorkNode>().isNull(WorkNode::getPriviousNodeId).eq(WorkNode::getFlowId,workNode.getFlowId()));
            if (ex != null && ex.getFlowId().equals(workNode.getFlowId())) {
                Long nodeId = saveNodeAndTask(workNode);
                return this.update(new LambdaUpdateWrapper<WorkNode>().set(WorkNode::getPriviousNodeId, nodeId).eq(BaseEntity::getId, ex.getId()));
            }
        }
        this.removeNodeCache(workNode.getFlowId());
        return saveNodeAndTask(workNode) != null;
    }

    private Long saveNodeAndTask(WorkNodeDTO workNode) {
        workNode.setName(WorkflowNodeEnum.valueOf(workNode.getCode()).getMsg());
        this.save(workNode);
        Long nodeId = workNode.getId();
        List<WorkNodeTask> tasks = workNode.getTasks();
        tasks.stream().forEach(x -> {
            x.setNodeId(nodeId);
            HandlerPermissionDTO handlerPermissionDTO = new HandlerPermissionDTO();
            handlerPermissionDTO.setName(x.getName());
            handlerPermissionDTO.setPowerId(x.getPowerId());
            x.setHandlerPermission(JSON.toJSONString(handlerPermissionDTO));
            x.setInfo(x.getInfo());
            x.setBusinessType(workNode.getBusinessType());
        });
        workNodeTaskService.saveBatch(tasks);
        return nodeId;
    }

    @Transactional
    @Override
    public Boolean update(WorkNodeDTO workNode) {
        List<WorkNodeTask> tasks = workNode.getTasks();
        //更新节点上的任务
        workNodeTaskService.update(new LambdaUpdateWrapper<WorkNodeTask>().eq(WorkNodeTask::getNodeId, workNode.getId()).set(WorkNodeTask::getVersion, NodeTaskVersion.HISTORY.name()));
        tasks.stream().forEach(x -> {
            x.setNodeId(workNode.getId());
            HandlerPermissionDTO handlerPermissionDTO = new HandlerPermissionDTO();
            handlerPermissionDTO.setName(x.getName());
            handlerPermissionDTO.setPowerId(x.getPowerId());
            x.setHandlerPermission(JSON.toJSONString(handlerPermissionDTO));
            x.setInfo(x.getInfo());
            x.setVersion(NodeTaskVersion.LATEST.name());
            x.setBusinessType(workNode.getBusinessType());
        });
        this.removeNodeCache(workNode.getFlowId());
        workNodeTaskService.saveBatch(tasks);
        return this.updateById(workNode);
    }

    @Transactional
    @Override
    public Boolean delete(Long id) {
        //逻辑删除
        WorkNode workNode = this.getById(id);
        WorkNode ex = this.getOne(new LambdaQueryWrapper<WorkNode>().eq(WorkNode::getPriviousNodeId, id));
        if (ex != null) {
            if (workNode.getPriviousNodeId() == null) {
                ex.setPriviousNodeId(null);
                this.updateById(ex);
            } else {
                ex.setPriviousNodeId(workNode.getPriviousNodeId());
                this.updateById(ex);
            }
        }
        workNodeTaskService.remove(new LambdaQueryWrapper<WorkNodeTask>().eq(WorkNodeTask::getNodeId, id));
        this.removeNodeCache(workNode.getFlowId());
        return this.removeById(id);
    }


    @Override
    public List<NameCodeVO> allNode() {
        List<WorkflowNodeEnum> enums = Arrays.asList(WorkflowNodeEnum.values());
        return enums.stream().map(x -> {
            NameCodeVO nameCodeVO = new NameCodeVO();
            nameCodeVO.setCode(x.getCode());
            nameCodeVO.setName(x.getMsg());
            return nameCodeVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<WorkNodeDTO> getFlowNodes(Long flowId, String businessType) {
        List<WorkNodeDTO> workflowNodes = workflowCache.getIfPresent(flowId);
        //先从缓存中取
        if (CollectionUtil.isEmpty(workflowNodes)) {
            workflowNodes = workNodeMapper.getFlowNodes(flowId, businessType);
            List<WorkNodeDTO> nodesSorted = Lists.newArrayList();
            Optional<WorkNodeDTO> first = workflowNodes.stream().filter(x -> x.getPriviousNodeId() == null).findFirst();
            if (first.isPresent()) {
                WorkNodeDTO fistNode = first.get();
                nodesSorted.add(fistNode);
                for (int i = 0; i < workflowNodes.size(); i++) {
                    WorkNodeDTO next = findNext(fistNode.getId(), workflowNodes);
                    if (next == null) break;
                    nodesSorted.add(next);
                    fistNode = next;
                }
            }
            workflowCache.put(flowId, nodesSorted);
            return nodesSorted;
        }
        return workflowNodes;
    }

    @Override
    public List<WorkNodeDTO> getFlowNodes(Long flowId) {
        List<WorkNodeDTO> workflowNodes = workflowCache.getIfPresent(flowId);
        if (CollectionUtil.isEmpty(workflowNodes)) {
            WorkFlow workFlow = workFlowMapper.selectOne(new LambdaQueryWrapper<WorkFlow>().eq(BaseEntity::getId, flowId)
                    .select(BaseEntity::getId, WorkFlow::getCode).eq(BaseEntity::getDeleted, Boolean.FALSE));
            return this.getFlowNodes(workFlow.getId(), workFlow.getCode());
        }
        return workflowNodes;
    }

    @Override
    public WorkNodeDTO getNextWorkNode(Long flowId, Long nodeId) {
        List<WorkNodeDTO> flowNodes = getFlowNodes(flowId);
        WorkNodeDTO next = findNext(nodeId, flowNodes);
        return next;
    }

    @Override
    public WorkNodeDTO getNextWorkNodeByBusiness(Long flowId, MainBusinessEnum mainBusinessEnum, String nodeCode, Map<String, List<WorkflowNodeEnum>> necessaryNodes,Boolean ifPay) {
        List<WorkNodeDTO> flowNodes = getFlowNodes(flowId);
        //报价客户反馈 后有可能出价值意见书、报告、复评函
        if (WorkflowNodeEnum.QUOTATION_FEEDBACK.name().equals(nodeCode)) {
            for (Map.Entry<String, List<WorkflowNodeEnum>> map : necessaryNodes.entrySet()) {
                List<String> codes = map.getValue().stream().map(WorkflowNodeEnum::getCode).collect(Collectors.toList());
                for (WorkNodeDTO workNodeDTO : flowNodes) {
                    if (codes.contains(workNodeDTO.getCode())) {
                        return workNodeDTO;
                    }
                }

            }
        }
        // 产品选择节点 后有可能出价值意见书、报告、意见函
        if (WorkflowNodeEnum.PRODUCT_CHOICE.name().equals(nodeCode)) {
            for (Map.Entry<String, List<WorkflowNodeEnum>> map : necessaryNodes.entrySet()) {
                List<String> codes = map.getValue().stream().map(WorkflowNodeEnum::getCode).collect(Collectors.toList());
                for (WorkNodeDTO workNodeDTO : flowNodes) {
                    if (codes.contains(workNodeDTO.getCode())) {
                        return workNodeDTO;
                    }
                }
            }
        }
        //价值意见书送达反馈 后有可能报告、复评函、不再出具（进入归档）
        if (WorkflowNodeEnum.STATEMENT_FEEDBACK.name().equals(nodeCode)) {
            necessaryNodes.remove(ProductionEnum.STATEMENT.name());
            if (necessaryNodes.size() != 0 ) {
                for (Map.Entry<String, List<WorkflowNodeEnum>> map : necessaryNodes.entrySet()) {
                    List<String> codes = map.getValue().stream().map(WorkflowNodeEnum::getCode).collect(Collectors.toList());
                    for (WorkNodeDTO workNodeDTO : flowNodes) {
                        if (codes.contains(workNodeDTO.getCode())) {
                            return workNodeDTO;
                        }
                    }
                }
           }
            //要收钱就需要归档
            if (ifPay!=null && ifPay){
                //归档
                return flowNodes.stream().filter(x -> x.getCode().equals(WorkflowNodeEnum.BUSINESS_ARCHIVING.name())).findFirst().get();
            }
        }
        //报告出库后或复评函出库后
        if (WorkflowNodeEnum.REPORT_OUT.name().equals(nodeCode) || WorkflowNodeEnum.LETTER_OUT.name().equals(nodeCode)) {
            if (mainBusinessEnum.equals(MainBusinessEnum.MAJOR_BUSINESS)) {
                //大中型业务 ：归档审核
                return flowNodes.stream().filter(x -> x.getCode().equals(WorkflowNodeEnum.CHECK_ARCHIVING.name())).findFirst().get();
            }
            if (mainBusinessEnum.equals(MainBusinessEnum.PERSONAL_BUSINESS)) {
                //个贷业务 ：归档
                return flowNodes.stream().filter(x -> x.getCode().equals(WorkflowNodeEnum.BUSINESS_ARCHIVING.name())).findFirst().get();
            }

        }
        return null;
    }

    @Override
    public WorkNodeDTO getOpeningOrderNode(Long flowId) {
        List<WorkNodeDTO> flowNodes = this.getFlowNodes(flowId);
        if (CollectionUtil.isNotEmpty(flowNodes)) {
            return flowNodes.stream().filter(x -> x.getPriviousNodeId() == null).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public WorkNodeDTO getNextWorkNodeByCheckState(Long flowId, String nodeCode, String doSecondCheck, String doThirdCheck) {
        List<WorkNodeDTO> flowNodes = getFlowNodes(flowId);
        for (int i = 0; i < flowNodes.size(); i++) {
            if (nodeCode.equals(CHECK_STATEMENT.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doSecondCheck)) {
                return flowNodes.get(i + 3);
            }
            if (nodeCode.equals(CHECK_STATEMENT.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doSecondCheck)) {
                return flowNodes.get(i + 1);
            }
            if ((nodeCode.equals(CHECK_REPORT.name()) || nodeCode.equals(LAND_REPORT_NOT_RECORD_CHECK.name())) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doSecondCheck)) {
                return flowNodes.get(i + 3 );
            }
            if ((nodeCode.equals(CHECK_REPORT.name()) || nodeCode.equals(LAND_REPORT_NOT_RECORD_CHECK.name())) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doSecondCheck)) {
                return flowNodes.get(i + 1 );
            }
            if (nodeCode.equals(CHECK_LETTER.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doSecondCheck)) {
                return flowNodes.get(i + 3);
            }
            if (nodeCode.equals(CHECK_LETTER.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doSecondCheck)) {
                return flowNodes.get(i + 1);
            }
            if (nodeCode.equals(RECHECK_STATEMENT.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 2);
            }
            if (nodeCode.equals(RECHECK_STATEMENT.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 1);
            }
            if ((nodeCode.equals(CHECK_REPORT.name()) || nodeCode.equals(LAND_REPORT_NOT_RECORD_CHECK.name())) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 2 );
            }
            if ((nodeCode.equals(CHECK_REPORT.name()) || nodeCode.equals(LAND_REPORT_NOT_RECORD_CHECK.name())) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 1 );
            }
            if (nodeCode.equals(RECHECK_REPORT.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 2);
            }
            if (nodeCode.equals(RECHECK_REPORT.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 1);
            }
            if (nodeCode.equals(RECHECK_LETTER.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.不审.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 2);
            }
            if (nodeCode.equals(RECHECK_LETTER.name()) && flowNodes.get(i).getCode().equals(nodeCode) && ReportStatus.待审核.name().equals(doThirdCheck)) {
                return flowNodes.get(i + 1);
            }
        }
        return null;
    }

    /**
     * 资产节点跳过判断方法
     * @param flowId 流程id
     * @param nodeCode 节点code
     * @return WorkNodeDTO
     */
    @Override
    public WorkNodeDTO getNextWorkNodeByFourthCheckState(Long flowId, String nodeCode) {
        List<WorkNodeDTO> flowNodes = getFlowNodes(flowId);
        for (int i = 0; i < flowNodes.size(); i++) {
            // 当前为复审报告节点时跳过四审报告节点
            if (nodeCode.equals(RECHECK_REPORT.name()) && flowNodes.get(i).getCode().equals(nodeCode)) {
                return flowNodes.get(i + 2);
            }
            // 当前为报告入库节点时跳过出库申请节点与报告出库部门审核节点
            if (nodeCode.equals(REPORT_IN.name()) && flowNodes.get(i).getCode().equals(nodeCode)) {
                return flowNodes.get(i + 3);
            }
        }
        return null;
    }

    @Override
    public List<WorkNode> getNodesByMainBusiness(String businessType) {
        List<WorkNode> list = workNodeMapper.getNodesByMainBusiness(businessType);
        return list;
    }

    @Override
    public void removeNodeCache(Long flowId) {
        workflowCache.invalidate(flowId);
    }

    @Override
    public WorkNode getUniqueNodeByFlowNameAndNodeName(MainBusinessEnum businessEnum, WorkflowNodeEnum nodeEnum) {
        return workNodeMapper.getUniqueNodeByFlowNameAndNodeName(businessEnum.name(),nodeEnum.name());
    }


    private WorkNodeDTO findNext(Long priviousId, List<WorkNodeDTO> nodes) {
        Optional<WorkNodeDTO> target = nodes.stream().filter(n -> n.getPriviousNodeId() != null)
                .filter(x -> (x.getPriviousNodeId().equals(priviousId))).findFirst();
        return target.orElse(null);
    }
}
