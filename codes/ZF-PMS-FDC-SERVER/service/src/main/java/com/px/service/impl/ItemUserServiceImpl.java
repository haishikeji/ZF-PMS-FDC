package com.px.service.impl;

import com.px.entity.ItemUser;
import com.px.mapper.ItemUserMapper;
import com.px.service.IItemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.vo.ItemStageUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 项目人员表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-12-08
 */
@Service
public class ItemUserServiceImpl extends ServiceImpl<ItemUserMapper, ItemUser> implements IItemUserService {

    @Autowired
    private ItemUserMapper itemUserMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<ItemUser> selectPage(Page page,ItemUser itemUser){
        return this.page(page, new QueryWrapper<ItemUser>(itemUser));
    }


    @Override
    public ItemUser detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(ItemUser itemUser){
        return  this.save(itemUser);
    }

    @Override
    public Boolean update(ItemUser itemUser){
        return  this.updateById(itemUser);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public List<ItemStageUserVO> userList(ItemStageUserVO itemStageUserVO) {
        List<ItemStageUserVO> list = itemUserMapper.userList(itemStageUserVO);
        return list;
    }
}
