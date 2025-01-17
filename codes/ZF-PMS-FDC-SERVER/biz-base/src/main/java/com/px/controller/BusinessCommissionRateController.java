package com.px.controller;

import com.px.common.PullDownModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IBusinessCommissionRateService;
import com.px.entity.BusinessCommissionRate;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 非土规业务提成比例
 *
 * @author 品讯科技
 * @since 2024-05-11
 */
@RestController
@RequestMapping("businessCommissionRate")
@Slf4j
public class BusinessCommissionRateController extends BaseController {
    @Autowired
    private IBusinessCommissionRateService businessCommissionRateService;

    /**
    * 非土规业务提成比例列表
    */
    @GetMapping("")
    public RestResponse<Page<BusinessCommissionRate>> page(BusinessCommissionRate businessCommissionRate, Page page){
        Page<BusinessCommissionRate> pages=businessCommissionRateService.selectPage(page,businessCommissionRate);
        return RestResponse.data(pages);
    }

    /**
     * 非土规业务提成比例详情
     */
    @GetMapping("/{id}")
    public RestResponse<BusinessCommissionRate> detail(@PathVariable Long id){
        BusinessCommissionRate xBusinessCommissionRate =businessCommissionRateService.detail(id);
        return RestResponse.data(xBusinessCommissionRate);
     }

    /**
     * 非土规业务提成比例新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody BusinessCommissionRate businessCommissionRate) {
        Boolean ret = businessCommissionRateService.add(businessCommissionRate);
        return RestResponse.data(ret);
    }

    /**
     * 非土规业务提成比例更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody BusinessCommissionRate businessCommissionRate) {
        Boolean ret = businessCommissionRateService.update(businessCommissionRate);
        return RestResponse.data(ret);
    }

    /**
     * 非土规业务提成比例删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = businessCommissionRateService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 获取申报业务子类
     * @param businessType
     * @param userType
     * @return
     */
    @GetMapping("/cate/{businessType}/{userType}")
    public RestResponse<List<PullDownModel>> getBusinessCate(@PathVariable("businessType") String businessType,@PathVariable("userType")String userType){
        List<PullDownModel> cate =   businessCommissionRateService.getBusinessCate(businessType,userType);
        return RestResponse.data(cate);
    }

}

