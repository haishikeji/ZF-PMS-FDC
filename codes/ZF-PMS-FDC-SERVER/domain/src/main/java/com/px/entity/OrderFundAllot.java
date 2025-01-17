package com.px.entity;
import java.math.BigDecimal;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 订单收款分配表
 * </p>
 *
 * @author 品讯科技
 * @since 2024-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "订单收款分配表")
public class OrderFundAllot extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 订单收款表id
     */
    private Long orderFundId;

    /**
     * 产品收款表id
     */
    private Long productionFundId;

    /**
     * 分配金额
     */
    @ImportCell
    @ExportCell(columnName = "分配金额")
    private BigDecimal allotAmount;

    /**
     * 实收款id
     */
    private Long realFundId;


}
