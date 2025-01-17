package com.px.service.workflow;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.BaseEntity;
import com.px.dto.WorkNodeCommit;
import com.px.dto.WorkNodeDTO;
import com.px.dto.WorkNodeInstanceTaskDTO;
import com.px.entity.WorkFlow;
import com.px.entity.WorkFlowNodeInstance;
import com.px.entity.WorkNodeTask;
import com.px.enums.MainBusinessEnum;
import com.px.enums.workflow.NodeLogEnum;
import com.px.enums.workflow.NodeStateEnum;
import com.px.exception.ErrorCode;
import com.px.mapper.WorkFlowMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.mapper.WorkTaskRecordMapper;
import com.px.utils.LoginContext;
import com.px.vo.NameCodeVO;
import com.px.workflow.handler.AssetsNodeHandler;
import com.px.workflow.handler.MajorNodeHandler;
import com.px.workflow.config.WorkNodeProcessable;
import com.px.workflow.handler.PersonalNodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.px.enums.MainBusinessEnum.*;
import static com.px.enums.workflow.NodeLogEnum.*;

/**
 * <p>
 * 工作流 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
@Service
public class WorkFlowServiceImpl extends ServiceImpl<WorkFlowMapper, WorkFlow> implements IWorkFlowService {

    @Autowired
    private IWorkFlowNodeInstanceService workFlowNodeInstanceService;

    @Autowired
    private MajorNodeHandler majorNodeHandler;

    @Autowired
    private PersonalNodeHandler personalNodeHandler;

    @Autowired
    private AssetsNodeHandler assetsNodeHandler;

    @Autowired
    private IWorkNodeService workNodeService;

    @Autowired
    private IWorkTaskRecordService workTaskRecordService;

    @Autowired
    private WorkTaskRecordMapper workTaskRecordMapper;

    @Autowired
    private WorkNodeProcessable workNodeProcessable;


    @Override
    @SuppressWarnings("unchecked")
    public Page<WorkFlow> selectPage(Page page,WorkFlow workFlow){
        return this.page(page, new QueryWrapper<WorkFlow>(workFlow));
    }


    @Override
    public WorkFlow detail(Long id){
        return this.getById(id);
    }

    @Override
    public Long add(WorkFlow workFlow){
        this.save(workFlow);
        return workFlow.getId();
    }

    @Override
    public Boolean update(WorkFlow workFlow){
        return  this.updateById(workFlow);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public List<NameCodeVO> flowsEnum() {
        List<MainBusinessEnum> enums = Arrays.asList(MainBusinessEnum.values());
        return enums.stream().map(x -> {
            NameCodeVO nameCodeVO = new NameCodeVO();
            nameCodeVO.setCode(x.getCode());
            nameCodeVO.setName(x.getMsg());
            return nameCodeVO;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean commit(WorkNodeCommit workNodeCommit) {
        WorkFlowNodeInstance instanceNode = workFlowNodeInstanceService.getById(workNodeCommit.getInstanceNodeId());
        if (instanceNode==null){
            ErrorCode.throwBusinessException(ErrorCode.CURRENT_NODE_NOT_EXISTS);
        }

        // 获取当前节点的任务信息
        List<WorkNodeInstanceTaskDTO> taskDTOs = workTaskRecordMapper.instanceNodeTasks(workNodeCommit.getInstanceNodeId());
        // 判断当前用户是否为节点任务处理人
        if (taskDTOs.stream().noneMatch(taskDTO -> taskDTO.getHandlerId().equals(LoginContext.getCurrentUserId()))){
            ErrorCode.throwBusinessException(ErrorCode.PERMISSION_ERROR);
        }

        //校验节点提交权限
//        Set<Long> handlerIds = workTaskRecordMapper.instanceNodeTasks(workNodeCommit.getInstanceNodeId()).stream().map(WorkNodeInstanceTaskDTO::getHandlerId).collect(Collectors.toSet());
//        if (!handlerIds.contains(LoginContext.getCurrentUserId())){
//            ErrorCode.throwBusinessException(ErrorCode.PERMISSION_ERROR);
//        }
        MainBusinessEnum businessEnum = MainBusinessEnum.valueOf(instanceNode.getBusinessType());

        if (businessEnum.equals(MAJOR_BUSINESS)){
            return handleByState(majorNodeHandler,workNodeCommit);
        }
        if (businessEnum.equals(PERSONAL_BUSINESS)){
            return handleByState(personalNodeHandler,workNodeCommit);
        }
        if (businessEnum.equals(ASSET_BUSINESS)){
            return handleByState(assetsNodeHandler,workNodeCommit);
        }
        return handleByState(workNodeProcessable,workNodeCommit);
    }

    /**
     * 批量提交流程
     * @param workNodeCommits 提交信息集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchCommit(List<WorkNodeCommit> workNodeCommits) {
        for (WorkNodeCommit workNodeCommit : workNodeCommits){
            Boolean success = commit(workNodeCommit);
            // 碰到提交失败的流程抛出异常
            if (!success){
                WorkFlowNodeInstance instanceNode = workFlowNodeInstanceService.getById(workNodeCommit.getInstanceNodeId());
                ErrorCode.throwBusinessException(ErrorCode.BATCH_COMMIT_ERROR,instanceNode.getBusinessId(), ObjectUtil.isNotNull(instanceNode.getBusinessMinId()) ? instanceNode.getBusinessMinId() : instanceNode.getBusinessSubId());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 业务下单
     * @param businessId
     * @param remark
     */
    @Override
    public WorkNodeCommit openingOrder(MainBusinessEnum mainBusinessEnum,Long businessId,String businessSubId, String remark){
        WorkFlow businessFlow = this.getOne(new LambdaQueryWrapper<WorkFlow>()
                .eq(WorkFlow::getCode, mainBusinessEnum.getCode())
                .eq(WorkFlow::getState, Boolean.FALSE)
                .eq(BaseEntity::getDeleted, Boolean.FALSE));

        if (businessFlow == null){
            ErrorCode.throwBusinessException(ErrorCode.WORKFLOW_NOT_EXISTS,mainBusinessEnum.getMsg());
        }

        WorkNodeDTO openingOrderNode = workNodeService.getOpeningOrderNode(businessFlow.getId());
        if (openingOrderNode == null){
            ErrorCode.throwBusinessException(ErrorCode.OPENING_NODE_NOT_EXISTS,mainBusinessEnum.getMsg());
        }

        WorkFlowNodeInstance instance = new WorkFlowNodeInstance();
        instance.setFlowId(businessFlow.getId());
        instance.setNodeId(openingOrderNode.getId());
        instance.setBusinessId(businessId);
        instance.setBusinessSubId(businessSubId);
        instance.setBusinessType(mainBusinessEnum.getCode());
        instance.setSequence(0);
        instance.setState(NodeStateEnum.PENDING.name());
        instance.setComment(remark);
        workFlowNodeInstanceService.save(instance);

        List<WorkNodeTask> tasks = openingOrderNode.getTasks();


        if (CollectionUtil.isNotEmpty(tasks)){
            if (mainBusinessEnum.equals(MAJOR_BUSINESS)){
                //业务成功下单后，将下单任务标记已完成。
                if (workTaskRecordService.doCreateNodeTaskRecord(majorNodeHandler,tasks, instance, LoginContext.getCurrentUserId())){
                    workTaskRecordMapper.finishOpening(instance.getId(),businessFlow.getId());
                }
            }else{
                //业务成功下单后，将下单任务标记已完成。
                if (workTaskRecordService.doCreateNodeTaskRecord(personalNodeHandler,tasks, instance, LoginContext.getCurrentUserId())){
                    workTaskRecordMapper.finishOpening(instance.getId(),businessFlow.getId());
                }
            }

        }

        //构造下单提交对象
        WorkNodeCommit commit = new WorkNodeCommit();
        commit.setInstanceNodeId(instance.getId());
        commit.setBusinessSubId(businessSubId);
        commit.setComments(remark);
        commit.setState(NodeLogEnum.PASS);
        return commit;
    }

    @Override
    public Boolean openingDeclare(MainBusinessEnum mainBusinessEnum, Long businessId) {
        WorkFlow businessFlow = this.getOne(new LambdaQueryWrapper<WorkFlow>()
                .eq(WorkFlow::getCode, mainBusinessEnum.getCode())
                .eq(WorkFlow::getState, Boolean.FALSE)
                .eq(BaseEntity::getDeleted, Boolean.FALSE));

        if (businessFlow == null){
            ErrorCode.throwBusinessException(ErrorCode.WORKFLOW_NOT_EXISTS,mainBusinessEnum.getMsg());
        }

        WorkNodeDTO firstNode = workNodeService.getOpeningOrderNode(businessFlow.getId());
        if (firstNode == null){
            ErrorCode.throwBusinessException(ErrorCode.OPENING_NODE_NOT_EXISTS,mainBusinessEnum.getMsg());
        }

        WorkFlowNodeInstance instance = new WorkFlowNodeInstance();
        instance.setFlowId(businessFlow.getId());
        instance.setNodeId(firstNode.getId());
        instance.setBusinessId(businessId);
        instance.setBusinessType(mainBusinessEnum.getCode());
        instance.setSequence(0);
        instance.setState(NodeStateEnum.PENDING.name());
        workFlowNodeInstanceService.save(instance);

        List<WorkNodeTask> tasks = firstNode.getTasks();

        if (CollectionUtil.isNotEmpty(tasks)){
            workTaskRecordService.doCreateNodeTaskRecord(workNodeProcessable,tasks,instance,null);
        }
        return Boolean.TRUE;
    }

    private Boolean handleByState(WorkNodeProcessable workNodeProcessable,WorkNodeCommit workNodeCommit){
        NodeLogEnum state = workNodeCommit.getState();
        if (state == null){
            ErrorCode.throwBusinessException(ErrorCode.COMMIT_STATE_ERROR);
        }
        if (state.equals(PASS)){
            return workNodeProcessable.forward(workNodeCommit);
        }
        if (state.equals(REVERSE)){
            return workNodeProcessable.goBack(workNodeCommit);
        }
        if (state.equals(RESTART)){
            return workNodeProcessable.reset(workNodeCommit);
        }
        if (state.equals(TERMINATE)){
            return workNodeProcessable.ending(workNodeCommit);
        }
        if (state.equals(SKIP)){
            return workNodeProcessable.skip(workNodeCommit);
        }
        return Boolean.FALSE;
    }
}
