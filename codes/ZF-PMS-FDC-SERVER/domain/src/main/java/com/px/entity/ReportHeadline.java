package com.px.entity;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 报告标题
 * </p>
 *
 * @author 品讯科技
 * @since 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "报告标题")
public class ReportHeadline extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 模板占位符
     */
    @ImportCell
    @ExportCell(columnName = "模板占位符")
    private String placeholder;

    /**
     * 标题
     */
    @ImportCell
    @ExportCell(columnName = "标题")
    private String headline;

    /**
     * 标题号
     */
    @ImportCell
    @ExportCell(columnName = "标题号")
    private String headlineNum;

    /**
     * 样式
     */
    @ImportCell
    @ExportCell(columnName = "样式")
    private String style;

    /**
     * 父标题id
     */
    private Long parentId;

    /**
     * 根节点id
     */
    private Long rootId;

    /**
     * 序号
     */
    @ImportCell
    @ExportCell(columnName = "序号")
    private Integer orderNum;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 算法名称
     */
    private String action;


}
