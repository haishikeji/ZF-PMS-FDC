package com.px.brokerage.constants;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/3/3
 * created with IntelliJ IDEA.
 */
public enum BrokerageRule {

    LAND_MARKETER_RULE("土地规划部市场人员规则","LAND_MARKETER_RULE"),
    LAND_SUPERVISOR_RULE("土地规划部营销主管规则","LAND_SUPERVISOR_RULE" ),

    LAND_MANAGER_RULE("土地规划部营销经理规则","LAND_MANAGER_RULE" ),
    LAND_OTHER_RULE("土地规划部参与人员规则","LAND_OTHER_RULE"),
    ;

    private String name;

    private String code;


    BrokerageRule(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static BrokerageRule brokerageRule(String brokerageRule){
        return BrokerageRule.valueOf(brokerageRule);
    }
}
