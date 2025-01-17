package com.px.service.impl;

import com.px.dto.PaymentDetailDTO;
import com.px.entity.BrokerageVariable;
import com.px.mapper.BrokerageVariableMapper;
import com.px.service.IBrokerageVariableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.IPaymentCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 浮动提成 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-04-14
 */
@Service
public class BrokerageVariableServiceImpl extends ServiceImpl<BrokerageVariableMapper, BrokerageVariable> implements IBrokerageVariableService {

    @Autowired
    private IPaymentCollectionService paymentCollectionService;

    @Autowired
    private BrokerageVariableMapper brokerageVariableMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<BrokerageVariable> selectPage(Page page,BrokerageVariable brokerageVariable){
        return this.page(page, new QueryWrapper<BrokerageVariable>(brokerageVariable));
    }


    @Override
    public BrokerageVariable detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(BrokerageVariable brokerageVariable){
        return  this.save(brokerageVariable);
    }

    @Override
    public Boolean update(BrokerageVariable brokerageVariable){
        return  this.updateById(brokerageVariable);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public List<PaymentDetailDTO> getPaymentDetailListByItemId(Long id,Long userId) {
        List<PaymentDetailDTO> list = paymentCollectionService.getPaymentDetailListByItemId(id,userId);
        return list;
    }

    @Override
    public List<BrokerageVariable> getCompensationBrokerage(Long userId,Long generalId) {
        return brokerageVariableMapper.getCompensationBrokerage(userId,generalId);
    }
}
