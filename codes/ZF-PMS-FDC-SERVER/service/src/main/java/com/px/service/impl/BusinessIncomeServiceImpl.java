package com.px.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.mapper.BusinessIncomeMapper;
import com.px.service.IBusinessIncomeService;
import com.px.utils.LoginContext;
import com.px.vo.UserCommissionProdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2024/5/31
 * created with IntelliJ IDEA.
 */
@Service
public class BusinessIncomeServiceImpl implements IBusinessIncomeService {

    @Autowired
    private BusinessIncomeMapper businessIncomeMapper;
    @Override
    public Page<UserCommissionProdVO> myMajorIncome(Page page, UserCommissionProdVO vo) {
        Long currentUserId = LoginContext.getCurrentUserId();
        vo.setUserId(currentUserId);
        Page<UserCommissionProdVO> ret = businessIncomeMapper.myMajorIncome(page,vo);
        return ret;
    }

    /**
     * 资产业务我的提成
     * @param page 分页信息
     * @param vo 查询条件
     * @return Page<UserCommissionProdVO>
     */
    @Override
    public Page<UserCommissionProdVO> myAssetsIncome(Page page, UserCommissionProdVO vo) {
        Long currentUserId = LoginContext.getCurrentUserId();
        vo.setUserId(currentUserId);
        Page<UserCommissionProdVO> ret = businessIncomeMapper.myAssetsIncome(page,vo);
        return ret;
    }

    /**
     * 资产业务我的提成到处excel
     * @param vo 查询条件
     * @return Page<UserCommissionProdVO>
     */
    @Override
    public List<UserCommissionProdVO> myAssetsIncomeExport(UserCommissionProdVO vo) {
        Long currentUserId = LoginContext.getCurrentUserId();
        vo.setUserId(currentUserId);
        return businessIncomeMapper.myAssetsIncomeExport(vo);
    }
}
