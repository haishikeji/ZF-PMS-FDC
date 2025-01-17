package com.px.entity;
import com.px.common.BaseEntity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 商机
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "商机")
public class BusinessOpportunity extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 机会名称
     */
    @ImportCell
    @ExportCell(columnName = "机会名称")
    private String opportunityName;

    /**
     * 预计金额
     */
    @ImportCell
    @ExportCell(columnName = "预计金额")
    private Double estimateAmount;

    /**
     * 预计开展日期
     */
    @ImportCell
    @ExportCell(columnName = "预计开展日期")
    private LocalDate estimateDate;

    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String remark;

    /**
     * 状态
     */
    @ImportCell
    @ExportCell(columnName = "状态")
    private String state;

    /**
     * 终止原因
     */
    @ImportCell
    @ExportCell(columnName = "终止原因")
    private String offReason;

    /**
     * 所属员工id
     */
    private Long userId;


}
