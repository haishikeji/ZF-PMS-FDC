package com.px.service;
import com.px.dto.BusinessOpportunityDTO;
import com.px.entity.BusinessOpportunity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.BusinessOpportunityVO;

/**
 * <p>
 * 商机 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
public interface IBusinessOpportunityService extends IService<BusinessOpportunity> {

        Page<BusinessOpportunityVO> selectPage(Page page, BusinessOpportunityVO businessOpportunity,String keyword);

        BusinessOpportunity detail(Long id);

        Boolean add(BusinessOpportunity businessOpportunity);

        Boolean update(BusinessOpportunity businessOpportunity);

        Boolean delete(Long id);

    Boolean changeManager(BusinessOpportunityDTO opportunity);
}
