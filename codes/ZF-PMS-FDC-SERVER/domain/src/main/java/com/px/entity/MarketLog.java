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
 * 市场人员日志
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "市场人员日志")
public class MarketLog extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 日志内容
     */
    @ImportCell
    @ExportCell(columnName = "日志内容")
    private String taskSituation;

    /**
     * 日志日期
     */
    @ImportCell
    @ExportCell(columnName = "日志日期")
    private LocalDate logDate;


}
