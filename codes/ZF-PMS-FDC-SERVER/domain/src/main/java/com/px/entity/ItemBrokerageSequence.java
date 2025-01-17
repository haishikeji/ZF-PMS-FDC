package com.px.entity;
import java.math.BigDecimal;
import com.px.common.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 项目提成流水
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "项目提成流水")
@Builder
public class ItemBrokerageSequence extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 提成总金额
     */
    @ImportCell
    @ExportCell(columnName = "提成总金额")
    private BigDecimal brokerageAmount;

    /**
     * 提成体现
     */
    @ImportCell
    @ExportCell(columnName = "提成体现")
    private String embody;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String remark;

    /**
     * 结算类型（项目结算，默认预提）
     */
    private String cate;
}
