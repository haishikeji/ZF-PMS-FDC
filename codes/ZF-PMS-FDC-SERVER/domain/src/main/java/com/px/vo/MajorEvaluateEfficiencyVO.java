package com.px.vo;

import com.px.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MajorEvaluateEfficiencyVO {

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String userName;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String departmentName;

    /**
     * 评估目的
     */
    @Excel(name = "评估目的")
    private String evaluateAim;

    /**
     * 是否金融(金融/非金融)
     */
    @Excel(name = "是否金融", readConverterExp="true=金融,false=非金融")
    private Boolean financial;

    /**
     * 未看现场数
     */
    @Excel(name = "未看现场数")
    private Integer unSiteCount;

    /**
     * 当期口估量
     */
    @Excel(name = "当期口估量")
    private Integer informalEvaluateCount;

    /**
     * 当期非口估量
     */
    @Excel(name = "当期非口估量")
    private Integer unInformalEvaluateCount;

    /**
     * 同比接单数量
     */
    @Excel(name = "同比接单增减率(%)")
    @JsonProperty("YoYOrderVolume")
    private Double YoYOrderVolume;

    /**
     * 环比接单数量
     */
    @Excel(name = "环比接单增减率(%)")
    @JsonProperty("QoQOrderVolume")
    private Double QoQOrderVolume;

    /**
     * 当期预评出具数量(意见书)
     */
    @Excel(name = "当期预评出具数量")
    private Integer currentPeriodStatementVolume;

    /**
     * 同比预评出具数量
     */
    @Excel(name = "同比预评出具增减率(%)")
    @JsonProperty("YoYStatementVolume")
    private Double YoYStatementVolume;

    /**
     * 环比预评出具数量
     */
    @Excel(name = "环比预评出具增减率(%)")
    @JsonProperty("QoQStatementVolume")
    private Double QoQStatementVolume;

    /**
     * 当期报告(评估报告、咨询报告、意见函)出具数量
     */
    @Excel(name = "当期报告出具数量")
    private Integer currentPeriodReportVolume;

    /**
     * 同比报告(评估报告、咨询报告、意见函)出具数量
     */
    @Excel(name = "同比报告出具增减率(%)")
    @JsonProperty("YoYReportVolume")
    private Double YoYReportVolume;

    /**
     * 环比报告(评估报告、咨询报告、意见函)出具数量
     */
    @Excel(name = "环比报告出具增减率(%)")
    @JsonProperty("QoQReportVolume")
    private Double QoQReportVolume;

    /**
     * 当期应收款
     */
    @Excel(name = "当期应收款")
    private Double currentPeriodShouldAmountSum;

    /**
     * 同比应收款
     */
    @Excel(name = "同比应收款增减率(%)")
    @JsonProperty("YoYShouldAmountSum")
    private Double YoYShouldAmountSum;

    /**
     * 环比应收款
     */
    @Excel(name = "环比应收款增减率(%)")
    @JsonProperty("QoQShouldAmountSum")
    private Double QoQShouldAmountSum;

    /**
     * 当期实收款
     */
    @Excel(name = "当期实收款")
    private Double currentPeriodRealAmountSum;

    /**
     * 同比实收款
     */
    @Excel(name = "同比实收款增减率(%)")
    @JsonProperty("YoYRealAmountSum")
    private Double YoYRealAmountSum;

    /**
     * 环比实收款
     */
    @Excel(name = "环比实收款增减率(%)")
    @JsonProperty("QoQRealAmountSum")
    private Double QoQRealAmountSum;

    /**
     * 实收款总计
     */
    @Excel(name = "实收款总计")
    private Double totalRealAmountSum;

    /**
     * 当期出单率
     */
    @Excel(name = "当期出单率")
    private Double currentPeriodOrderHasReportVolume;

    /**
     * 同比出单率(报告数/接单数)
     */
    @Excel(name = "同比出单率")
    @JsonProperty("YoYOrderHasReportVolume")
    private Double YoYOrderHasReportVolume;

    /**
     * 环比出单率(报告数/接单数)
     */
    @Excel(name = "环比出单率")
    @JsonProperty("QoQOrderHasReportVolume")
    private Double QoQOrderHasReportVolume;

    /**
     * 累计年口估数
     */
    @Excel(name = "累计年口估数")
    private Integer yearInformalEvaluateCount;

    /**
     * 意见书平均内控作业时长(AICP:Average Internal Control Processing)
     */
    @Excel(name = "意见书平均内控作业时长")
    private Double statementAICPTime;

    /**
     * 报告平均内控作业时长(AICP:Average Internal Control Processing)
     */
    @Excel(name = "报告平均内控作业时长")
    private Double reportAICPTime;

    /**
     * 报价时长(金融)
     */
    @Excel(name = "报价时长(金融)")
    private Double financialQuoteProcessingTime;

    /**
     * 报价时长(非金融)
     */
    @Excel(name = "报价时长(非金融)")
    private Double unFinancialQuoteProcessingTime;

}
