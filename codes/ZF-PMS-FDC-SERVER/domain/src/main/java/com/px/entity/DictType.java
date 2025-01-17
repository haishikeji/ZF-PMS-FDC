package com.px.entity;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 字典类型
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "字典类型")
public class DictType extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 字典类型名称
     */
    @ImportCell
    @ExportCell(columnName = "字典类型名称")
    private String name;

    /**
     * 字典类型
     */
    @ImportCell
    @ExportCell(columnName = "字典类型")
    private String dictType;

    /**
     * 字典编号
     */
    @ImportCell
    @ExportCell(columnName = "字典编号")
    private String code;

    /**
     * 字典描述
     */
    @ImportCell
    @ExportCell(columnName = "字典描述")
    private String remark;


}
