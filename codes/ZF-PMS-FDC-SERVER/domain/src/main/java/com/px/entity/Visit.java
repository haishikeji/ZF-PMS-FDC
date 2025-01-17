package com.px.entity;
import com.px.annotation.Excel;
import com.px.common.BaseEntity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 拜访记录表
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "拜访记录表")
public class Visit extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 拜访类型（初访、回访）
     */
    @Excel( name="拜访类型")
    private String visitType;

    /**
     * 拜访时间
     */
    @Excel( name="拜访时间")
    private LocalDate visitTime;

    /**
     * 拜访地点
     */
    @ImportCell
    @ExportCell(columnName = "拜访地点")
    private String spot;

    /**
     * 沟通内容
     */
    @Excel( name="沟通内容")
    private String content;

    /**
     * 所属员工id
     */
    private Long userId;


}
