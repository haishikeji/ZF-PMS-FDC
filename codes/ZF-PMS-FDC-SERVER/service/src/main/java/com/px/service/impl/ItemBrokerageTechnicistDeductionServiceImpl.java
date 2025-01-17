package com.px.service.impl;

import com.px.entity.ItemBrokerageTechnicistDeduction;
import com.px.mapper.ItemBrokerageTechnicistDeductionMapper;
import com.px.service.IItemBrokerageTechnicistDeductionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.LoginContext;
import com.px.vo.DeductionTechStatVO;
import com.px.vo.TechDeductionVO;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;

/**
 * <p>
 * 技术人员提成抵扣记录 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-06-06
 */
@Service
public class ItemBrokerageTechnicistDeductionServiceImpl extends ServiceImpl<ItemBrokerageTechnicistDeductionMapper, ItemBrokerageTechnicistDeduction> implements IItemBrokerageTechnicistDeductionService {

    @Autowired
    private ItemBrokerageTechnicistDeductionMapper itemBrokerageTechnicistDeductionMapper;

    @Autowired
    private LoadingCache<Long,BigDecimal> baseAmountsCache;

    @Override
    @SuppressWarnings("unchecked")
    public Page<TechDeductionVO> selectPage(Page page, TechDeductionVO techDeductionVO){
        techDeductionVO.setUserId(LoginContext.getUserId());
        Page<TechDeductionVO> xPage = itemBrokerageTechnicistDeductionMapper.xPage(page,techDeductionVO);
        return xPage;
    }


    @Override
    public ItemBrokerageTechnicistDeduction detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(ItemBrokerageTechnicistDeduction itemBrokerageTechnicistDeduction){
        return  this.save(itemBrokerageTechnicistDeduction);
    }

    @Override
    public Boolean update(ItemBrokerageTechnicistDeduction itemBrokerageTechnicistDeduction){
        return  this.updateById(itemBrokerageTechnicistDeduction);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public DeductionTechStatVO stat() {
        Long userId = LoginContext.getUserId();
        BigDecimal poolResidueAmount =  itemBrokerageTechnicistDeductionMapper.poolResidueAmount(userId);
        DeductionTechStatVO vo = DeductionTechStatVO
                .builder()
                .poolResidueAmount(poolResidueAmount)
                .defaultBrokerageAmount(baseAmountsCache.getIfPresent(userId))
                .build();
        return vo;
    }
}
