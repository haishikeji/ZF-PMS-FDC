package com.px.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 非土规业务提成比例
 * </p>
 *
 * @author 品讯科技
 * @since 2024-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "非土规业务提成比例")
public class BusinessCommissionRate extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 字典数据表业务类型id
     */
    private String businessType;

    /**
     * 字典数据表人员类型id
     */
    private String userType;

    /**
     * 字典数据表业务子类id
     */
    private Long businessCateId;

    /**
     * 最小提成比例
     */
    @ImportCell
    @ExportCell(columnName = "最小提成比例")
    private BigDecimal minRatio;

    /**
     * 最大提成比例
     */
    @ImportCell
    @ExportCell(columnName = "最大提成比例")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal maxRatio;

    /**
     * 提成下线金额
     */
    @ImportCell
    @ExportCell(columnName = "提成下线金额")
    private BigDecimal lowLimitAmount;

    /**
     * 提成上线金额
     */
    @ImportCell
    @ExportCell(columnName = "提成上线金额")
    private BigDecimal topLimitAmount;

    /**
     * 季度业绩提成比例
     */
    @ImportCell
    @ExportCell(columnName = "季度业绩提成比例")
    private BigDecimal quarterRatio;

    /**
     * 业务子类名称
     */
    @TableField(exist = false)
    private String businessCateName;
}
