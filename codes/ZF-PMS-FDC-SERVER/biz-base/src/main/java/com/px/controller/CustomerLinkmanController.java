package com.px.controller;

import com.px.common.BaseEntity;
import com.px.utils.LoginContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.ICustomerLinkmanService;
import com.px.entity.CustomerLinkman;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import com.px.common.PullDownModel;
import com.px.utils.ConvertUtil;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 企业客户联系人
 *
 * @author 品讯科技
 * @since 2023-12-01
 */
@RestController
@RequestMapping("customerLinkman")
@Slf4j
public class CustomerLinkmanController extends BaseController {
    @Autowired
    private ICustomerLinkmanService customerLinkmanService;

    /**
    * 企业客户联系人列表
    */
    @GetMapping("")
    public RestResponse<Page<CustomerLinkman>> page(CustomerLinkman customerLinkman, Page page){
        Page<CustomerLinkman> pages=customerLinkmanService.selectPage(page,customerLinkman);
        return RestResponse.data(pages);
    }

    /**
     * 企业客户联系人详情
     */
    @GetMapping("/{id}")
    public RestResponse<CustomerLinkman> detail(@PathVariable Long id){
        CustomerLinkman xCustomerLinkman =customerLinkmanService.detail(id);
        return RestResponse.data(xCustomerLinkman);
     }

    /**
     * 企业客户联系人新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody CustomerLinkman customerLinkman) {
        Boolean ret = customerLinkmanService.add(customerLinkman);
        return RestResponse.data(ret);
    }

    /**
     * 企业客户联系人更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody CustomerLinkman customerLinkman) {
        Boolean ret = customerLinkmanService.update(customerLinkman);
        return RestResponse.data(ret);
    }

    /**
     * 企业客户联系人删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = customerLinkmanService.delete(id);
        return RestResponse.data(ret);
    }


    /**
     * 企业客户联系人下拉列表
     */
    @PostMapping("/simpleAll")
    public RestResponse<List<PullDownModel>> simpleAll(@RequestBody CustomerLinkman customerLinkman){
        LambdaQueryWrapper<CustomerLinkman> lambdaQueryWrapper = new LambdaQueryWrapper<CustomerLinkman>()
        .select(CustomerLinkman::getId,CustomerLinkman::getName)
        .eq(CustomerLinkman::getCcId,customerLinkman.getCcId())
        .eq(CustomerLinkman::getClientManagerId,LoginContext.getCurrentUserId())
        .eq(customerLinkman.getTerminal()!=null,CustomerLinkman::getTerminal,customerLinkman.getTerminal())
        .eq(CustomerLinkman::getDeleted,false).orderByDesc(BaseEntity::getId);
        List<CustomerLinkman> list= customerLinkmanService.list(lambdaQueryWrapper);
        List<PullDownModel> ret=ConvertUtil.copyList(list, PullDownModel.class);
        return RestResponse.data(ret);
    }
}

