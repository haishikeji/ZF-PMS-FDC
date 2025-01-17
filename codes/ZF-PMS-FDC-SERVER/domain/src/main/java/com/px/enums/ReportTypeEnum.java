package com.px.enums;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/9/11
 * created with IntelliJ IDEA.
 */
public enum ReportTypeEnum {

    /**
     * 社会稳定性风险评价报告
     */
    LAND_RISK_FTL("LAND_RISK_FTL"),

    /**
     * 土地定级与基准地价更新技术报告
     */
    LAND_CITY_PRICE("LAND_CITY_PRICE")
    ;

    private String reportType;

    ReportTypeEnum(String reportType) {
        this.reportType = reportType;
    }
}
