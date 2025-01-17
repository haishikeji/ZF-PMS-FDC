package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.dto.MajorProductionSimpleDTO;
import com.px.entity.FinanceInvoice;
import com.px.dao.CustomBaseMapper;
import com.px.vo.FinanceInvoiceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 财务开票 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-08
 */
public interface FinanceInvoiceMapper extends CustomBaseMapper<FinanceInvoice> {

    Page<FinanceInvoiceVo> getPage(Page page, @Param("vo") FinanceInvoice financeInvoice, @Param("keyword") String keyword);

    /**
     * 开票信息导出
     * @param financeInvoice 查询条件
     * @param keyword 关键
     * @return List<FinanceInvoiceVo>
     */
    List<FinanceInvoiceVo> exportPage(@Param("vo") FinanceInvoice financeInvoice, @Param("keyword") String keyword);

    List<FinanceInvoiceVo> getInvoiceAmountInfo(@Param("dto") MajorProductionSimpleDTO simpleDTO);

    List<FinanceInvoiceVo> getInvoiceAmountInfoEmptyProduction(@Param("dto") MajorProductionSimpleDTO simpleDTO);
}
