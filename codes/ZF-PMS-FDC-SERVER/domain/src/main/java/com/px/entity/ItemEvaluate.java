package com.px.entity;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;

import java.math.BigDecimal;

/**
 * <p>
 * 项目完结评价表
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "项目完结评价表")
public class ItemEvaluate extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 项目id
     */
    private Long itemId;

    /**
     * 项目整体提成权重
     */
    @ImportCell
    @ExportCell(columnName = "项目整体提成权重")
    private BigDecimal weight;

    /**
     * 项目评价
     */
    @ImportCell
    @ExportCell(columnName = "项目评价")
    private String comments;


}
