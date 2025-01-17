package com.px.service;
import com.px.entity.ProductionFund;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.ProductionFundVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 业务产品收款表 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-08
 */
public interface IProductionFundService extends IService<ProductionFund> {

        Page<ProductionFund> selectPage(Page page,ProductionFund productionFund);

        ProductionFund detail(Long id);

        Boolean add(ProductionFund productionFund);

        Boolean update(ProductionFund productionFund);

        Boolean delete(Long id);

        Boolean updateRealFundForProduction(Long id);

        ProductionFundVO checkFund(ProductionFundVO productionFundVO);

        BigDecimal calculateStandardAmount(BigDecimal evaluateAmount);

        BigDecimal getProductionRealAmountByProductionNo(String productionNo);

        void updateProductionRealAmount(Long productionFundId , BigDecimal rollbackAmount);

        List<ProductionFund> selectProductionFundIdByDyoaReportNo();

        List<ProductionFund> getProductionFundIdByOrderFundId();
}
