package com.px.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.px.common.BaseEntity;
import com.px.entity.CustomerCompany;
import com.px.mapper.CustomerCompanyMapper;
import com.px.service.ICustomerCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.LoginContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * <p>
 * 企业客户 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-01
 */
@DS("mbs")
@Service
public class CustomerCompanyServiceImpl extends ServiceImpl<CustomerCompanyMapper, CustomerCompany> implements ICustomerCompanyService {

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<CustomerCompany> selectPage(Page page,CustomerCompany customerCompany){
        return this.page(page, new QueryWrapper<CustomerCompany>(customerCompany));
    }


    @Override
    public CustomerCompany detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(CustomerCompany customerCompany){
        customerCompany.setClientManagerId(LoginContext.getCurrentUserId());
        return  this.save(customerCompany);
    }

    @Override
    public Boolean update(CustomerCompany customerCompany){
        return  this.updateById(customerCompany);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public List<CustomerCompany> simpleAll(CustomerCompany customer) {
        LambdaQueryWrapper<CustomerCompany> lambdaQueryWrapper = new LambdaQueryWrapper<CustomerCompany>();
        lambdaQueryWrapper.select(CustomerCompany::getId,CustomerCompany::getName);
        if (customer.getParentId()!=null){
            lambdaQueryWrapper.eq(CustomerCompany::getParentId,customer.getParentId());
        }else{
            lambdaQueryWrapper.isNull(CustomerCompany::getParentId);
            lambdaQueryWrapper.ne(CustomerCompany::getName,"个人客户");
        }
        if (customer.getTerminal()){
            lambdaQueryWrapper.eq(CustomerCompany::getClientManagerId, LoginContext.getCurrentUserId());
        }
        lambdaQueryWrapper.eq(CustomerCompany::getTerminal,customer.getTerminal());
        lambdaQueryWrapper.eq(BaseEntity::getDeleted,Boolean.FALSE);
        lambdaQueryWrapper.orderByDesc(BaseEntity::getId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean avoidWareHouseApply(Long id) {
        CustomerCompany cc = this.getOne(new LambdaQueryWrapper<CustomerCompany>().eq(BaseEntity::getId, id).isNull(CustomerCompany::getParentId)
                .eq(CustomerCompany::getTerminal, Boolean.FALSE).eq(CustomerCompany::getCompanyType, "银行及金融机构").eq(BaseEntity::getDeleted, Boolean.FALSE));
        return cc!=null;
    }

    @Override
    public Boolean avoidWareHouseApplyByTargetId(Long targetId) {
        Long ccId = customerCompanyMapper.avoidWareHouseApplyByTargetId(targetId);
        return ccId!=null;
    }
}
