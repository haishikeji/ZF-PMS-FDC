package com.px.controller;

import com.px.dto.TaskRecordDTO;
import com.px.vo.ProductionOutWarehouseApplyVO;
import com.px.workflow.annotation.FinishTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IProductionOutWarehouseService;
import com.px.entity.ProductionOutWarehouse;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

/**
 * 产品提前出库申请
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
@RestController
@RequestMapping("productionOutWarehouse")
@Slf4j
public class ProductionOutWarehouseController extends BaseController {
    @Autowired
    private IProductionOutWarehouseService productionOutWarehouseService;


    /**
    * 产品提前出库申请列表
    */
    @GetMapping("")
    public RestResponse<Page<ProductionOutWarehouse>> page(ProductionOutWarehouse productionOutWarehouse, Page page,String keyword){
        Page<ProductionOutWarehouse> pages=productionOutWarehouseService.selectPage(page,productionOutWarehouse,keyword);
        return RestResponse.data(pages);
    }

    /**
     * 产品提前出库申请详情
     */
    @GetMapping("/{id}")
    public RestResponse<ProductionOutWarehouse> detail(@PathVariable Long id){
        ProductionOutWarehouse xProductionOutWarehouse =productionOutWarehouseService.detail(id);
        return RestResponse.data(xProductionOutWarehouse);
     }

    /**
     * 产品提前出库申请新增
     */
    @FinishTask
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody @Valid TaskRecordDTO<ProductionOutWarehouse> taskRecordDTO) {
        Boolean ret = productionOutWarehouseService.add(taskRecordDTO);
        return RestResponse.data(ret);
    }

    /**
     * 产品提前出库申请更新
     */
    @FinishTask
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody TaskRecordDTO<ProductionOutWarehouse> taskRecordDTO) {
        Boolean ret = productionOutWarehouseService.update(taskRecordDTO);
        return RestResponse.data(ret);
    }

    /**
     * 产品提前出库申请删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = productionOutWarehouseService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 出库申请列表
     * @param id
     * @return
     */
    @GetMapping("/apply/{id}")
    public RestResponse<List<ProductionOutWarehouseApplyVO>> getList(@PathVariable("id") Long id){
        List<ProductionOutWarehouseApplyVO> list = productionOutWarehouseService.getList(id);
        return RestResponse.data(list);
    }

    /**
     * 大中型出库申请列表 (业务类型和产品id查询)
     * @return
     */
    @GetMapping("/production/{businessType}/{productionId}")
    public RestResponse<List<ProductionOutWarehouseApplyVO>> getListByBizTypeProductionId(@PathVariable("businessType") String businessType,
                                                                                   @PathVariable("productionId") Long productionId){
        List<ProductionOutWarehouseApplyVO> list = productionOutWarehouseService.getListByBizTypeProductionId(businessType,productionId);
        return RestResponse.data(list);
    }

    /**
     * 个贷出库申请列表
     * @return
     */
    @GetMapping("/personal/{orderId}")
    public RestResponse<List<ProductionOutWarehouseApplyVO>> getPersonalByOrderId(@PathVariable("orderId") Long orderId){
        List<ProductionOutWarehouseApplyVO> list = productionOutWarehouseService.getPersonalByOrderId(orderId);
        return RestResponse.data(list);
    }

    /**
     * 撤销申请
     * @param id
     * @return
     */
    @GetMapping("/cancel/{id}")
    public RestResponse<Boolean> cancel(@PathVariable("id") Long id){
       Boolean ret =  productionOutWarehouseService.cancel(id);
       return RestResponse.data(ret);
    }
}

