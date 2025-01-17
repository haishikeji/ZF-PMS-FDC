package com.px.service.impl;

import com.px.entity.BusinessFile;
import com.px.mapper.BusinessFileMapper;
import com.px.service.IBusinessFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 业务文档 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
@Service
public class BusinessFileServiceImpl extends ServiceImpl<BusinessFileMapper, BusinessFile> implements IBusinessFileService {


    @Override
    @SuppressWarnings("unchecked")
    public Page<BusinessFile> selectPage(Page page,BusinessFile businessFile){
        return this.page(page, new QueryWrapper<BusinessFile>(businessFile));
    }


    @Override
    public BusinessFile detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(BusinessFile businessFile){
        return  this.save(businessFile);
    }

    @Override
    public Boolean update(BusinessFile businessFile){
        return  this.updateById(businessFile);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }
}
