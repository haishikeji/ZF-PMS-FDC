package com.px.entity;
import java.math.BigDecimal;

import com.px.common.BaseEntity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 资产评估对象表
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "资产评估对象表")
public class AssetsEvaluationTarget extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 资产评估目的id
     */
    private Long evaluationPurposeId;

    /**
     * 资产评估对象类型一级id
     */
    private Long evaluationTypeId;

    /**
     * 资产评估对象类型二级id
     */
    private Long evaluationTypeSecId;

    /**
     * 其他自定义二级资产评估对象类型
     */
    private String evaluationTypeDefinedSec;

    /**
     * 产权持有人
     */
    @ImportCell
    @ExportCell(columnName = "产权持有人")
    private String theHolder;

    /**
     * 资产价值类型id
     */
    private Long assetsValueId;

    /**
     * 项目负责人id (user_id)
     */
    private Long principalId;

    /**
     * 实勘人id (json 可多个)
     */
    @ImportCell
    @ExportCell(columnName = "实勘人id (json 可多个)")
    private String siteIds;

    /**
     * 估价基准日
     */
    @ImportCell
    @ExportCell(columnName = "估价基准日")
    private LocalDate valuationBasisDate;

    /**
     * 评估方法id
     */
    private String evaluationMethodId;

    /**
     * 最终选择的评估方法id
     */
    private String choiceEvaluationMethodId;

    /**
     * 总资产账面值(万)
     */
    @ImportCell
    @ExportCell(columnName = "总资产账面值(万)")
    private BigDecimal totalAssetCarryingAmount;

    /**
     * 总负债账面值(万)
     */
    @ImportCell
    @ExportCell(columnName = "总负债账面值(万)")
    private BigDecimal totalLiabilitiesCarryingAmount;

    /**
     * 净资产账面值(万)
     */
    @ImportCell
    @ExportCell(columnName = "净资产账面值(万)")
    private BigDecimal carryingAmountOfNetAssets;

    /**
     * 评估结论对应的评估值(万)
     */
    @ImportCell
    @ExportCell(columnName = "评估结论对应的评估值(万)")
    private BigDecimal estimatedValue;

    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String remark;

    /**
     * 资料路由信息
     */
    @ImportCell
    @ExportCell(columnName = "资料路由信息")
    private String filePath;

    /**
     * 资产业务id
     */
    private Long assetsId;

    /**
     * 审核价格
     */
    @ImportCell
    @ExportCell(columnName = "审核价格")
    private BigDecimal checkValue;

    /**
     * 意见书产品号
     */
    private String statementNo;

    /**
     * 报告产品号
     */
    private String reportNo;

    /**
     * 自定义其他评估方法
     */
    private String definedEvaluationMethod;

    /**
     * 最终选择的自定义其他评估方法
     */
    private String choiceDefinedEvaluationMethod;


}
