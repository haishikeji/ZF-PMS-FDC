package com.px.vo;

import com.px.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonalDepEfficiencyVO {

    /**
     * 评估目的
     */
    @Excel(name = "评估目的")
    private String evaluateAim;

    /**
     * 当期部门接单量
     */
    @Excel(name = "当期部门接单量")
    private Integer currentPeriodOrderVolume;

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
     * 当期出单率(报告数/接单数)
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
     * 当期死单率(%)
     */
    @Excel(name = "当期死单率(%)")
    @JsonProperty("currentPeriodDeadOrderVolume")
    private Double currentPeriodDeadOrderVolume;

    /**
     * 同比死单率(%)
     */
    @Excel(name = "同比死单率(%)")
    @JsonProperty("YoYDeadOrderVolume")
    private Double YoYDeadOrderVolume;

    /**
     * 环比死单率(%)
     */
    @Excel(name = "环比死单率(%)")
    @JsonProperty("QoQDeadOrderVolume")
    private Double QoQDeadOrderVolume;

    /**
     * 当期实收款订单数
     */
    private Integer currentPeriodRealAmountOrderCount;

    /**
     * 同比实收款订单数
     */
    private Integer YoYRealAmountOrderCount;

    /**
     * 环比实收款订单数
     */
    private Integer QoQRealAmountOrderCount;
}
