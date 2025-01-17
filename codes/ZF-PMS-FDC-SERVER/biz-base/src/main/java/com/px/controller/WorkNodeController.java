package com.px.controller;

import com.px.dto.WorkNodeDTO;
import com.px.vo.NameCodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.workflow.IWorkNodeService;
import com.px.entity.WorkNode;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import com.px.common.PullDownModel;
import com.px.utils.ConvertUtil;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 工作流节点
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
@RestController
@RequestMapping("workNode")
@Slf4j
public class WorkNodeController extends BaseController {
    @Autowired
    private IWorkNodeService workNodeService;

    /**
    * 工作流节点列表
    */
    @GetMapping("")
    public RestResponse<Page<WorkNode>> page(WorkNode workNode, Page page){
        Page<WorkNode> pages=workNodeService.selectPage(page,workNode);
        return RestResponse.data(pages);
    }

    /**
     * 工作流节点详情
     */
    @GetMapping("/{id}")
    public RestResponse<WorkNode> detail(@PathVariable Long id){
        WorkNode xWorkNode =workNodeService.detail(id);
        return RestResponse.data(xWorkNode);
     }

    /**
     * 工作流节点新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody WorkNodeDTO workNode) {
        Boolean ret = workNodeService.add(workNode);
        return RestResponse.data(ret);
    }

    /**
     * 工作流节点更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody WorkNodeDTO workNode) {
        Boolean ret = workNodeService.update(workNode);
        return RestResponse.data(ret);
    }

    /**
     * 工作流节点删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = workNodeService.delete(id);
        return RestResponse.data(ret);
    }


    /**
     * 工作流节点下拉列表
     */
    @GetMapping("/simpleAll")
    public RestResponse<List<PullDownModel>> simpleAll(){
        LambdaQueryWrapper<WorkNode> lambdaQueryWrapper = new LambdaQueryWrapper<WorkNode>()
        .select(WorkNode::getId,WorkNode::getName)
        .eq(WorkNode::getDeleted,false);
        List<WorkNode> list= workNodeService.list(lambdaQueryWrapper);
        List<PullDownModel> ret=ConvertUtil.copyList(list, PullDownModel.class);
        return RestResponse.data(ret);
    }

    /**
     * 节点枚举
     * @return
     */
    @GetMapping("/enum")
    public RestResponse<List<NameCodeVO>> allNode(){
        List<NameCodeVO> enums = workNodeService.allNode();
        return RestResponse.data(enums);
    }

    /**
     * 单流程节点
     * @param flowId
     * @return
     */
    @GetMapping("/flow/{flowId}")
    public RestResponse<List<WorkNodeDTO>> getFlowNodes(@PathVariable("flowId") Long flowId){
        List<WorkNodeDTO> nodes = workNodeService.getFlowNodes(flowId);
        return RestResponse.data(nodes);
    }

    /**
     * 根据业务类型获取节点列表
     * @param businessType
     * @return
     */
    @GetMapping("/by/{businessType}")
    public RestResponse<List<WorkNode>> getNodesByMainBusiness(@PathVariable("businessType") String businessType){
        List<WorkNode> nodes = workNodeService.getNodesByMainBusiness(businessType);
        return RestResponse.data(nodes);
    }
}

