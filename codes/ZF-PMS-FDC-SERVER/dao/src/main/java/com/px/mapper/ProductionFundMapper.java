package com.px.mapper;

import com.px.dto.ReportDTO;
import com.px.entity.ProductionFund;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 业务产品收款表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-08
 */
public interface ProductionFundMapper extends CustomBaseMapper<ProductionFund> {

   List<ReportDTO> getProductionFundByOrderFundId(@Param("orderFundId") Long orderFundId);

    List<ProductionFund> deepQueryProductionFund(@Param("majorIds") List<Long> majorIds);

    void updateRealAmount(@Param("id") Long id, @Param("thisTimeAmount") BigDecimal thisTimeAmount);

    void updateProductionRealAmount(@Param("productionFundId") Long productionFundId, @Param("rollbackAmount") BigDecimal rollbackAmount);

    List<ProductionFund> selectProductionFundIdByDyoaReportNo();

    List<ProductionFund> getProductionFundIdByOrderFundId();
}
