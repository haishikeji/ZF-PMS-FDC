package com.px.mapper;

import com.px.entity.AssetsEvaluationTarget;
import com.px.dao.CustomBaseMapper;
import com.px.vo.AssetsEvaluationTargetVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产评估对象表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
public interface AssetsEvaluationTargetMapper extends CustomBaseMapper<AssetsEvaluationTarget> {

    /**
     * 根据资产业务订单id获取评估对象信息
     * @param id 资产订单id
     * @return List<AssetsEvaluationTargetVO>
     */
    List<AssetsEvaluationTargetVO> getAETargetListByAssetsId(@Param("id") Long id,@Param("statementNo") String statementNo,@Param("reportNo") String reportNo);

    /**
     * 评估对象添加意见书产品号
     * @param id 评估对象id
     * @param productionNo 产品号
     * @return BOolean
     */
    Boolean updateTargetStatementNo(@Param("id") Long id, @Param("productionNo") String productionNo);

    /**
     * 评估对象添加报告产品号
     * @param id 评估对象id
     * @param productionNo 产品号
     * @return BOolean
     */
    Boolean updateTargetReportNo(@Param("id") Long id, @Param("productionNo") String productionNo);

    /**
     * 根据业务id获取已取号评估对象的意见书产品号
     * @param businessId 资产业务id
     * @return List<String>
     */
    List<String> getStatementNoList(@Param("businessId") Long businessId);

    /**
     * 根据业务id获取已取号评估对象的报告产品号
     * @param businessId 资产业务id
     * @return List<String>
     */
    List<String> getReportNoList(@Param("businessId") Long businessId, @Param("targetIdList") List<Long> targetIdList);

    /**
     * 评估对象审核定价
     * @param assetsEvaluationTarget 审核定价信息
     * @return Boolean
     */
    Boolean checkValue(AssetsEvaluationTarget assetsEvaluationTarget);

    /**
     * 根据业务id查询该订单下的所有评估对象
     * @param businessId 业务id
     * @return Integer
     */
    Integer targetCount(@Param("businessId") Long businessId, @Param("statementNo") String statementNo);
}
