package com.px.service.impl;

import com.px.entity.UserArchive;
import com.px.mapper.UserArchiveMapper;
import com.px.service.IUserArchiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 员工档案 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-09-02
 */
@Service
public class UserArchiveServiceImpl extends ServiceImpl<UserArchiveMapper, UserArchive> implements IUserArchiveService {

    @Autowired
    private UserArchiveMapper userArchiveMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<UserArchive> selectPage(Page page,UserArchive userArchive){
        return this.page(page, new QueryWrapper<UserArchive>(userArchive));
    }


    @Override
    public UserArchive detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(UserArchive userArchive){
        return  this.save(userArchive);
    }

    @Override
    public Boolean update(UserArchive userArchive){
        return  this.updateById(userArchive);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public UserArchive getRegistrationNoByUserId(Long userId) {
        return  userArchiveMapper.getRegistrationNoByUserId(userId);
    }
}
