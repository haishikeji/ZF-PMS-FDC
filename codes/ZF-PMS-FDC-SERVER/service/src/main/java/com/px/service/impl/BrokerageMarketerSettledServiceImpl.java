package com.px.service.impl;

import com.px.entity.BrokerageMarketerSettled;
import com.px.mapper.BrokerageMarketerSettledMapper;
import com.px.service.IBrokerageMarketerSettledService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 客户经理已结算回款表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-09-26
 */
@Service
public class BrokerageMarketerSettledServiceImpl extends ServiceImpl<BrokerageMarketerSettledMapper, BrokerageMarketerSettled> implements IBrokerageMarketerSettledService {


    @Override
    @SuppressWarnings("unchecked")
    public Page<BrokerageMarketerSettled> selectPage(Page page,BrokerageMarketerSettled brokerageMarketerSettled){
        return this.page(page, new QueryWrapper<BrokerageMarketerSettled>(brokerageMarketerSettled));
    }


    @Override
    public BrokerageMarketerSettled detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(BrokerageMarketerSettled brokerageMarketerSettled){
        return  this.save(brokerageMarketerSettled);
    }

    @Override
    public Boolean update(BrokerageMarketerSettled brokerageMarketerSettled){
        return  this.updateById(brokerageMarketerSettled);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }
}
