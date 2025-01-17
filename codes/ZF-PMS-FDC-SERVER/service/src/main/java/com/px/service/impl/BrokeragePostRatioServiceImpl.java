package com.px.service.impl;

import com.px.entity.BrokeragePostRatio;
import com.px.mapper.BrokeragePostRatioMapper;
import com.px.service.IBrokeragePostRatioService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.vo.PostRatioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 领导岗位抽成率 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-10
 */
@Service
public class BrokeragePostRatioServiceImpl extends ServiceImpl<BrokeragePostRatioMapper, BrokeragePostRatio> implements IBrokeragePostRatioService {


    @Autowired
    private BrokeragePostRatioMapper postRatioMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<PostRatioVO> selectPage(Page page, PostRatioVO postRatioVO){
        Page<PostRatioVO> xPage = postRatioMapper.page(page,postRatioVO);
        return xPage;
    }


    @Override
    public BrokeragePostRatio detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(BrokeragePostRatio brokeragePostRatio){
        return  this.save(brokeragePostRatio);
    }

    @Override
    public Boolean update(BrokeragePostRatio brokeragePostRatio){
        return  this.saveOrUpdate(brokeragePostRatio);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }
}
