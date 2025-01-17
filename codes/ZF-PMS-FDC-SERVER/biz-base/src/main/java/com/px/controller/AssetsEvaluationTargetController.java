package com.px.controller;

import com.px.dto.AETargetListSelectDTO;
import com.px.dto.TaskRecordDTO;
import com.px.vo.AssetsEvaluationTargetVO;
import com.px.workflow.annotation.FinishTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IAssetsEvaluationTargetService;
import com.px.entity.AssetsEvaluationTarget;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产评估对象表
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
@RestController
@RequestMapping("assetsEvaluationTarget")
@Slf4j
public class AssetsEvaluationTargetController extends BaseController {
    @Autowired
    private IAssetsEvaluationTargetService assetsEvaluationTargetService;

    /**
     * 根据资产业务id获取资产业务评估对象集合
     * @param dto 查询dto
     * @return RestResponse<List<AssetsEvaluationTargetVO>>
     */
    @GetMapping("/getAETargetListByAssetsId")
    public RestResponse<List<AssetsEvaluationTargetVO>> getAETargetListByAssetsId(AETargetListSelectDTO dto) {
        return RestResponse.data(assetsEvaluationTargetService.getAETargetListByAssetsId(dto.getBusinessId(), dto.getStatementNo(), dto.getReportNo()));
    }

    /**
    * 资产评估对象表列表
    */
    @GetMapping("")
    public RestResponse<Page<AssetsEvaluationTarget>> page(AssetsEvaluationTarget assetsEvaluationTarget, Page page){
        Page<AssetsEvaluationTarget> pages=assetsEvaluationTargetService.selectPage(page,assetsEvaluationTarget);
        return RestResponse.data(pages);
    }

    /**
     * 资产评估对象表详情
     */
    @GetMapping("detail/{id}")
    public RestResponse<AssetsEvaluationTarget> detail(@PathVariable Long id){
        AssetsEvaluationTarget xAssetsEvaluationTarget =assetsEvaluationTargetService.detail(id);
        return RestResponse.data(xAssetsEvaluationTarget);
     }

    /**
     * 资产评估对象表新增
     */
    @FinishTask
    @PostMapping("/save")
    public RestResponse<Boolean> save(@RequestBody TaskRecordDTO<AssetsEvaluationTarget> taskRecordDTO) {
        Boolean ret = assetsEvaluationTargetService.add(taskRecordDTO.getTaskData());
        return RestResponse.data(ret);
    }

    /**
     * 资产评估对象表更新
     */
    @PutMapping("/update")
    public RestResponse<Boolean> update(@RequestBody AssetsEvaluationTarget assetsEvaluationTarget) {
        Boolean ret = assetsEvaluationTargetService.update(assetsEvaluationTarget);
        return RestResponse.data(ret);
    }

    /**
     * 资产评估对象表删除
     */
    @DeleteMapping("delete/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = assetsEvaluationTargetService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 评估对象审核定价
     * @param assetsEvaluationTarget 审核定价信息
     * @return RestResponse<Boolean>
     */
    @PutMapping("/checkValue")
    public RestResponse<Boolean> checkValue(@RequestBody AssetsEvaluationTarget assetsEvaluationTarget) {
        return RestResponse.data(assetsEvaluationTargetService.checkValue(assetsEvaluationTarget));
    }

}

