package com.px.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.px.entity.CustomerLinkman;
import com.px.mapper.CustomerLinkmanMapper;
import com.px.service.ICustomerLinkmanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.LoginContext;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 企业客户联系人 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-01
 */
@DS("mbs")
@Service
public class CustomerLinkmanServiceImpl extends ServiceImpl<CustomerLinkmanMapper, CustomerLinkman> implements ICustomerLinkmanService {


    @Override
    @SuppressWarnings("unchecked")
    public Page<CustomerLinkman> selectPage(Page page,CustomerLinkman customerLinkman){
        return this.page(page, new QueryWrapper<CustomerLinkman>(customerLinkman));
    }


    @Override
    public CustomerLinkman detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(CustomerLinkman customerLinkman){
        customerLinkman.setClientManagerId(LoginContext.getCurrentUserId());
        return  this.save(customerLinkman);
    }

    @Override
    public Boolean update(CustomerLinkman customerLinkman){
        return  this.updateById(customerLinkman);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }
}
