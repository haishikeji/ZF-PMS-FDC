package com.px.entity;
import com.px.annotation.*;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 岗位
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "岗位")
public class Post extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 名称
     */
    @ImportCell
    @ExportCell(columnName = "名称")
    private String name;

    /**
     * 所属部门id
     */
    private Long departmentId;

    /**
     * 岗位职责
     */
    @ImportCell
    @ExportCell(columnName = "岗位职责")
    private String duty;


    /**
     * 父级岗位id
     */
    @ImportCell
    @ExportCell(columnName = "父级岗位id")
    private Long parentId;
}
