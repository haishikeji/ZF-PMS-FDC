package com.px.service.impl;

import com.px.entity.PersonalExample;
import com.px.mapper.PersonalExampleMapper;
import com.px.service.IPersonalExampleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 个贷可比实例 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
@Service
public class PersonalExampleServiceImpl extends ServiceImpl<PersonalExampleMapper, PersonalExample> implements IPersonalExampleService {


    @Override
    @SuppressWarnings("unchecked")
    public Page<PersonalExample> selectPage(Page page,PersonalExample personalExample){
        return this.page(page, new QueryWrapper<PersonalExample>(personalExample));
    }


    @Override
    public PersonalExample detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(PersonalExample personalExample){
        return  this.save(personalExample);
    }

    @Override
    public Boolean update(PersonalExample personalExample){
        return  this.updateById(personalExample);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }
}
