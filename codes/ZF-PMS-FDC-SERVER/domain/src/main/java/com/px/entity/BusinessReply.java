package com.px.entity;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 业务回复表
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "业务回复表")
public class BusinessReply extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 业务类型
     */
    @ImportCell
    @ExportCell(columnName = "业务类型")
    private String bizType;

    /**
     * 业务表id
     */
    private Long bizTableId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 内容
     */
    @ImportCell
    @ExportCell(columnName = "内容")
    private String content;

    /**
     * 回复者id
     */
    private Long replierId;

    /**
     * 接受者id
     */
    @ImportCell
    @ExportCell(columnName = "接受者id")
    private Long receiverId;

    /**
     * 状态（已读，未读）
     */
    @ImportCell
    @ExportCell(columnName = "状态（已读，未读）")
    private Boolean state;


}
