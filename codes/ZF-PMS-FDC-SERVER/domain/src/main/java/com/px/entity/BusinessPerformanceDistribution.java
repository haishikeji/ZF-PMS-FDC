package com.px.entity;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;

import java.io.Serializable;

/**
 * <p>
 * 绩效分配比例信息
 * </p>
 *
 * @author 品讯科技
 * @since 2024-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "绩效分配比例信息")
public class BusinessPerformanceDistribution extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 业务id
     */
    private Long businessId;

    /**
     * 业务类型
     */
    @ImportCell
    @ExportCell(columnName = "业务类型")
    private String businessType;

    /**
     * 报告产品号
     */
    @ImportCell
    @ExportCell(columnName = "报告产品号")
    private String reportNo;

    /**
     * 成员id（user_id）
     */
    private Long memberId;

    /**
     * 每人比例信息
     */
    @ImportCell
    @ExportCell(columnName = "每人比例信息")
    private Double performanceDistribution;

    /**
     * 项目负责人、成员、签字师
     */
    private String memberType;

    /**
     * 提成申报id
     */
    private Long declareId;

    /**
     * 人员类型（市场人员/评估人员）
     */
    private String userType;

    /**
     * 人员参与时长（小时）
     */
    private Integer participationDuration;

}
