package com.px.brokerage.constants;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/3/3
 * created with IntelliJ IDEA.
 */
public enum BrokerageState {

    //参与人提成状态
    NOT_START("未开始","NOT_START"),
    PREDICTING("预提中","PREDICTING"),

    //客户经理提成状态
    TO_PREDICTING("待预提","TO_PREDICTING"),
    PREDICTED("已预提","PREDICTED"),

    NOT_PAYMENT("回款中","NOT_PAYMENT"),

    //公用状态
    FREEZE("冻结","FREEZE"),
    NOT_SETTLE("待结算","NOT_SETTLE"),
    CLOSED("已结算","CLOSED");
    ;

    private String name;

    private String code;

    BrokerageState(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
