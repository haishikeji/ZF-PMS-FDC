package com.px.service;
import com.px.dto.AssetsPerformanceDistributionAddDTO;
import com.px.dto.UserShareRate;
import com.px.entity.BusinessPerformanceDistribution;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 绩效分配比例信息 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-05-09
 */
public interface IBusinessPerformanceDistributionService extends IService<BusinessPerformanceDistribution> {

        Page<BusinessPerformanceDistribution> selectPage(Page page,BusinessPerformanceDistribution businessPerformanceDistribution);

        BusinessPerformanceDistribution detail(Long id);

        Boolean add(BusinessPerformanceDistribution businessPerformanceDistribution);

        Boolean update(BusinessPerformanceDistribution businessPerformanceDistribution);

        Boolean delete(Long id);

        /**
         * 新增资产绩效分配信息
         * @param dto 新增dto
         * @return Boolean
         */
        Boolean addAssetsPerformanceDistribution(AssetsPerformanceDistributionAddDTO dto);

        /**
         * 获取资产绩效分配信息list
         * @param dto 查询dto
         * @return List<AssetsPerformanceDistributionAddDTO>
         */
        List<BusinessPerformanceDistribution> getPerformanceDistributionList(AssetsPerformanceDistributionAddDTO dto);

        /**
         * 修改资产绩效分配信息
         * @param dto 修改dto
         * @return Boolean
         */
        Boolean updateAssetsPerformanceDistribution(AssetsPerformanceDistributionAddDTO dto);

        /**
         * 根据申报id获取绩效比列分配集合
         * @param declareId 申报id
         * @return List<UserShareRate>
         */
        List<UserShareRate> alloAssetsDistributionDeclareId(Long declareId);

}
