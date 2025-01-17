package com.px.service.impl;

import com.px.common.PullDownModel;
import com.px.entity.SortedDepartment;
import com.px.mapper.SortedDepartmentMapper;
import com.px.service.ISortedDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 轮单部门 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-31
 */
@Service
public class SortedDepartmentServiceImpl extends ServiceImpl<SortedDepartmentMapper, SortedDepartment> implements ISortedDepartmentService {

    @Autowired
    private SortedDepartmentMapper sortedDepartmentMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<SortedDepartment> selectPage(Page page,SortedDepartment sortedDepartment){
        return this.page(page, new QueryWrapper<SortedDepartment>(sortedDepartment));
    }


    @Override
    public SortedDepartment detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(SortedDepartment sortedDepartment){
        return  this.save(sortedDepartment);
    }

    @Override
    public Boolean update(SortedDepartment sortedDepartment){
        return  this.updateById(sortedDepartment);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public List<PullDownModel> simpleAll(String businessType) {
        return sortedDepartmentMapper.simpleAll(businessType);
    }
}
