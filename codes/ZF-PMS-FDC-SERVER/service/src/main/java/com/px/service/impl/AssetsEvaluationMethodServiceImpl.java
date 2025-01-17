package com.px.service.impl;

import com.px.entity.AssetsEvaluationMethod;
import com.px.mapper.AssetsEvaluationMethodMapper;
import com.px.service.IAssetsEvaluationMethodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 资产评估方法表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-04-07
 */
@Service
public class AssetsEvaluationMethodServiceImpl extends ServiceImpl<AssetsEvaluationMethodMapper, AssetsEvaluationMethod> implements IAssetsEvaluationMethodService {

    @Autowired
    private AssetsEvaluationMethodMapper assetsEvaluationMethodMapper;

    /**
     * 获取所有评估方法集合
     * @return List<AssetsEvaluationMethod>
     */
    @Override
    public List<AssetsEvaluationMethod> getAssetsEvaluationMethod() {
        return assetsEvaluationMethodMapper.getAssetsEvaluationMethod();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<AssetsEvaluationMethod> selectPage(Page page,AssetsEvaluationMethod assetsEvaluationMethod){
        return this.page(page, new QueryWrapper<AssetsEvaluationMethod>(assetsEvaluationMethod));
    }


    @Override
    public AssetsEvaluationMethod detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(AssetsEvaluationMethod assetsEvaluationMethod){
        return  this.save(assetsEvaluationMethod);
    }

    @Override
    public Boolean update(AssetsEvaluationMethod assetsEvaluationMethod){
        return  this.updateById(assetsEvaluationMethod);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }
}
