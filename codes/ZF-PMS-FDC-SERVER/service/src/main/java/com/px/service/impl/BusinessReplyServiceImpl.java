package com.px.service.impl;

import com.px.common.BaseEntity;
import com.px.entity.BusinessReply;
import com.px.mapper.BusinessReplyMapper;
import com.px.service.IBusinessReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.IMessageService;
import com.px.utils.LoginContext;
import com.px.vo.BusinessReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 业务回复表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-23
 */
@Service
public class BusinessReplyServiceImpl extends ServiceImpl<BusinessReplyMapper, BusinessReply> implements IBusinessReplyService {

    @Autowired
    private BusinessReplyMapper businessReplyMapper;

    @Autowired
    private IMessageService messageService;

    @Override
    @SuppressWarnings("unchecked")
    public Page<BusinessReplyVO> selectPage(Page page, BusinessReply businessReply){
        Long userId = LoginContext.getUserId();
        businessReplyMapper.updateState(userId,businessReply.getBizTableId(),businessReply.getBizType());
        return businessReplyMapper.getPage(page,businessReply);
    }


    @Override
    public BusinessReply detail(Long id){
        this.update(new LambdaUpdateWrapper<BusinessReply>().set(BusinessReply::getState,1).eq(BaseEntity::getId,id));
        return this.getById(id);
    }

    @Transactional
    @Override
    public Boolean reply(BusinessReply businessReply){
        this.save(businessReply);
        messageService.packagingBusinessMessage(businessReply);
        return  Boolean.TRUE;
    }

    @Override
    public Boolean update(BusinessReply businessReply){
        return  this.updateById(businessReply);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public Integer getNotReadCount(BusinessReply businessReply) {
        Integer count = this.count(new LambdaQueryWrapper<BusinessReply>().eq(BusinessReply::getBizType, businessReply.getBizType())
                .eq(BusinessReply::getBizTableId, businessReply.getBizTableId()).eq(BusinessReply::getReceiverId, businessReply.getReceiverId())
                        .eq(BusinessReply::getState, Boolean.FALSE).eq(BusinessReply::getDeleted, Boolean.FALSE));
        return count;
    }
}
