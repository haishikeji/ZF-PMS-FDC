package com.px.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.ICustomerCompanyService;
import com.px.entity.CustomerCompany;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import com.px.common.PullDownModel;
import com.px.utils.ConvertUtil;

import java.util.List;

/**
 * 企业客户
 *
 * @author 品讯科技
 * @since 2023-12-01
 */
@RestController
@RequestMapping("customerCompany")
@Slf4j
public class CustomerCompanyController extends BaseController {
    @Autowired
    private ICustomerCompanyService customerCompanyService;

    /**
    * 企业客户列表
    */
    @GetMapping("")
    public RestResponse<Page<CustomerCompany>> page(CustomerCompany customerCompany, Page page){
        Page<CustomerCompany> pages=customerCompanyService.selectPage(page,customerCompany);
        return RestResponse.data(pages);
    }

    /**
     * 企业客户详情
     */
    @GetMapping("/{id}")
    public RestResponse<CustomerCompany> detail(@PathVariable Long id){
        CustomerCompany xCustomerCompany =customerCompanyService.detail(id);
        return RestResponse.data(xCustomerCompany);
     }

    /**
     * 企业客户新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody CustomerCompany customerCompany) {
        Boolean ret = customerCompanyService.add(customerCompany);
        return RestResponse.data(ret);
    }

    /**
     * 企业客户更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody CustomerCompany customerCompany) {
        Boolean ret = customerCompanyService.update(customerCompany);
        return RestResponse.data(ret);
    }

    /**
     * 企业客户删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = customerCompanyService.delete(id);
        return RestResponse.data(ret);
    }


    /**
     * 企业客户下拉列表
     */
    @PostMapping("/simpleAll")
    public RestResponse<List<PullDownModel>> simpleAll(@RequestBody CustomerCompany customer){
        List<CustomerCompany> list= customerCompanyService.simpleAll(customer);
        List<PullDownModel> ret=ConvertUtil.copyList(list, PullDownModel.class);
        return RestResponse.data(ret);
    }
}

