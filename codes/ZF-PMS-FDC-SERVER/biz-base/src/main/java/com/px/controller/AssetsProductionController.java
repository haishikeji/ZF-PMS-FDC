package com.px.controller;

import cn.hutool.core.util.ObjectUtil;
import com.px.dto.*;
import com.px.vo.AssetsProductionFundVo;
import com.px.workflow.annotation.FinishTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IAssetsProductionService;
import com.px.entity.AssetsProduction;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

/**
 * 资产产品表
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
@RestController
@RequestMapping("assetsProduction")
@Slf4j
public class AssetsProductionController extends BaseController {
    @Autowired
    private IAssetsProductionService assetsProductionService;

    /**
     * 根据资产业务id查询资产业务产品
     * @param id 资产业务id
     * @return RestResponse<List<AssetsProduction>>
     */
    @GetMapping("getAroductionByAssetsId/{id}")
    public RestResponse<List<AssetsProduction>> getAroductionByAssetsId(@PathVariable Long id) {
        return RestResponse.data(assetsProductionService.getAroductionByAssetsId(id));
    }

    /**
     * 资产产品表列表
     */
    @GetMapping("")
    public RestResponse<Page<AssetsProduction>> page(AssetsProduction assetsProduction, Page page){
        Page<AssetsProduction> pages=assetsProductionService.selectPage(page,assetsProduction);
        return RestResponse.data(pages);
    }

    /**
     * 资产产品表详情
     */
    @GetMapping("detail/{id}")
    public RestResponse<AssetsProduction> detail(@PathVariable Long id){
        AssetsProduction xAssetsProduction =assetsProductionService.detail(id);
        return RestResponse.data(xAssetsProduction);
    }

    /**
     * 资产产品表新增
     */
    @FinishTask
    @PostMapping("/save")
    public RestResponse<Boolean> save(@RequestBody TaskRecordDTO<AssetsProduction> taskRecordDTO) {
        Boolean ret = assetsProductionService.add(taskRecordDTO.getTaskData());
        return RestResponse.data(ret);
    }

    /**
     * 资产产品选择类型表更新
     */
    @FinishTask
    @PutMapping("/update/choice")
    public RestResponse<Boolean> updateChoice(@RequestBody TaskRecordDTO<AssetsProduction> taskRecordDTO) {
        Boolean ret = assetsProductionService.updateChoice(taskRecordDTO.getTaskData());
        return RestResponse.data(ret);
    }

    /**
     * 产品信息更新
     * @param assetsProduction prod
     * @return RestResponse<Boolean>
     */
    @PutMapping("/update")
    public RestResponse<Boolean> update(@RequestBody AssetsProduction assetsProduction) {
        Boolean ret = assetsProductionService.update(assetsProduction);
        return RestResponse.data(ret);
    }

    /**
     * 资产产品表删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = assetsProductionService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 根据业务id获取产品信息类型集合
     * @param id 资产业务id
     * @return List<String>
     */
    @GetMapping("/getProductionTypes/{id}")
    public RestResponse<List<String>> getProductionTypes(@PathVariable Long id){
        return RestResponse.data(assetsProductionService.getProductionTypes(id));
    }

    /**
     * 根据业务id获取已选择产品
     * @param businessId 资产业务id
     * @return AssetsProduction
     */
    @GetMapping("/getProductionByBusinessId/{businessId}")
    public RestResponse<AssetsProduction> getProductionByBusinessId(@PathVariable Long businessId) {
        return RestResponse.data(assetsProductionService.getProductionByBusinessId(businessId));
    }

    /**
     * 撰写资产产品信息
     * @param taskRecordDTO 节点完成DTO
     * @return RestResponse<Boolean>
     */
    @FinishTask
    @PostMapping("/writeAssetsProduction")
    public RestResponse<Boolean> writeAssetsProduction(@RequestBody TaskRecordDTO<AssetsProduction> taskRecordDTO) {
        Boolean flag = assetsProductionService.writeAssetsProduction(taskRecordDTO.getTaskData());
        if (ObjectUtil.isNull(flag)){
            return RestResponse.success(flag,"该产品已出具!");
        }
        return RestResponse.data(flag);
    }

    /**
     * 根据业务id与产品号获取资产产品详细信息
     * @param dto 查询dto
     * @return AssetsProduction
     */
    @GetMapping("/getAssetsProductionDetail")
    public RestResponse<AssetsProduction> getAssetsProductionDetail(AssetsProductionDetailDTO dto) {
        return RestResponse.data(assetsProductionService.getAssetsProductionDetail(dto));
    }

    /**
     * 根据业务id与产品号查询产品信息
     * @param aProListSelectDTO 查询dto
     * @return List<AssetsProduction>
     */
    @PostMapping("/getProductionList")
    public RestResponse<List<AssetsProduction>> getProductionList(@RequestBody AProListSelectDTO aProListSelectDTO) {
        return RestResponse.data(assetsProductionService.getProductionList(aProListSelectDTO));
    }

    /**
     * 查询该产品是否已有提交的流程
     * @param aProListSelectDTO 查询dto
     * @return RestResponse<Integer>
     */
    @PostMapping("/getInatanceCount")
    public RestResponse<Integer> getInatanceCount(@RequestBody AProListSelectDTO aProListSelectDTO) {
        return RestResponse.data(assetsProductionService.getInatanceCount(aProListSelectDTO));
    }

    /**
     * 根据业务id与产品号集合获取每个产品是否有提交过的流程实例信息
     * @param aProListSelectDTO 查询dto
     * @return RestResponse<Integer>
     */
    @PostMapping("/getInatanceCountList")
    public RestResponse<List<Integer>> getInatanceCountList(@RequestBody AProListSelectDTO aProListSelectDTO) {
        return RestResponse.data(assetsProductionService.getInatanceCountList(aProListSelectDTO));
    }

    /**
     * 资产产品出入库
     * @param taskRecordDTO 节点完成DTO
     * @return RestResponse<Boolean>
     */
    @FinishTask
    @PutMapping("/assetsProRepertory")
    public RestResponse<Boolean> assetsProRepertory(@RequestBody @Valid TaskRecordDTO<AssetsProWareHouseDTO> taskRecordDTO) {
        return RestResponse.data(assetsProductionService.assetsProRepertory(taskRecordDTO.getTaskData()));
    }

    /**
     * 根据资产产品号更新文件路由信息
     * @param assetsProduction 产品信息dto
     * @return RestResponse<Boolean>
     */
    @PutMapping("/updateProductionFileInfo")
    public RestResponse<Boolean> updateProductionFileInfo(@RequestBody AssetsProduction assetsProduction) {
        return RestResponse.data(assetsProductionService.updateProductionFileInfo(assetsProduction));
    }

    /**
     * 根据产品号获取产品实收款
     * @param productionNo 产品号
     * @return RestResponse<BigDecimal>
     */
    @GetMapping("/getProductionRealAmount/{productionNo}")
    public RestResponse<BigDecimal> getProductionRealAmount(@PathVariable String productionNo) {
        return RestResponse.data(assetsProductionService.getProductionRealAmount(productionNo));
    }

    /**
     * 产品标记送达
     * @param productionNo 产品号
     * @return Boolean
     */
    @PutMapping("/markDelivery/{productionNo}")
    public RestResponse<Boolean> markDelivery(@PathVariable String productionNo) {
        return RestResponse.data(assetsProductionService.markDelivery(productionNo));
    }

    /**
     * 查找实收款认领产品列表
     * @param apFundSelectDTO 查询dto
     * @return RestResponse<Page<AssetsProductionFundVo>>
     */
    @GetMapping("/getAssetsProductionFundVoList")
    public RestResponse<Page<AssetsProductionFundVo>> getAssetsProductionFundVoList(Page page, APFundSelectDTO apFundSelectDTO) {
        return RestResponse.data(assetsProductionService.getAssetsProductionFundVoList(page, apFundSelectDTO));
    }

    /**
     * 查找实收款认领产品列表
     * @param apFundSelectDTO 查询dto
     * @return RestResponse<Page<AssetsProductionFundVo>>
     */
    @GetMapping("/getAssetsOrderFundVoList")
    public RestResponse<Page<AssetsProductionFundVo>> getAssetsOrderFundVoList(Page page, APFundSelectDTO apFundSelectDTO) {
        return RestResponse.data(assetsProductionService.getAssetsOrderFundVoList(page, apFundSelectDTO));
    }

    /**
     * 根据订单收款id获取产品收款信息
     * @param orderFundId 资产订单收款id
     * @return List<ReportDTO>
     */
    @GetMapping("/getProductionFundByOrderFundId/{orderFundId}")
    public RestResponse<List<ReportDTO>> getProductionFundByOrderFundId(@PathVariable Long orderFundId) {
        return RestResponse.data(assetsProductionService.getProductionFundByOrderFundId(orderFundId));
    }

    /**
     * 资产分配订单实收款到产品
     *
     * @param orderReportDTO 分配信息
     * @return Boolean
     */
    @PostMapping("/allotOrderRealAmountToProduction")
    public RestResponse<Boolean> allotOrderRealAmountToProduction(@RequestBody OrderReportDTO orderReportDTO) {
        return RestResponse.data(assetsProductionService.allotOrderRealAmountToProduction(orderReportDTO));
    }

}

