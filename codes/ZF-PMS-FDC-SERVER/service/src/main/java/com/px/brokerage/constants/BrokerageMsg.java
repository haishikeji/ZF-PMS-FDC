package com.px.brokerage.constants;

import java.math.BigDecimal;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/3/14
 * created with IntelliJ IDEA.
 */
public class BrokerageMsg {

    public static final String BROKERAGE_SEQUENCE_REMARK = "系统每月23日零点自动结算项目提成,您上月可结算（包含预提）项目个数:[sum]个。";

    public static final String POOL_SETTLE_REMARK = "系统每月23日零点自动结算阶段流程已完成且已被评价的项目,结算金额已转移至个人提成奖金池。";
    public static final String NON_MARKETER_BASE_SEQUENCE_REMARK = "技术人员默认提成,此提成与个人提成奖金池剩余金额抵扣。";

    public static final String NON_MARKETER_ALL_PAYMENT_REMARK = "此项目已全额回款，其提成金额若有剩余将一次性发放。";

    public static final String BROKERAGE_SEQUENCE_EMBODY = "[year-month]薪资";

    public static final String BROKERAGE_POOL_EMBODY = "[year-month]转移";

    /**
     * 客户经理预提比例
     */
    public static final BigDecimal MARKETER_AHEAD_SCALE = new BigDecimal("0.4");

    /**
     * 营销主管固定提成系数
     */
    public static final BigDecimal SUPERVISOR_DEFAULT_BROKERAGE_SCALE = new BigDecimal("0.4");

    /**
     * 营销主管浮动提成系数
     */
    public static final BigDecimal SUPERVISOR_BROKERAGE_SCALE = new BigDecimal("0.5");

    /**
     * 系统自定义一年月份数
     */
    public static final BigDecimal MONTH_OF_YEAR = BigDecimal.TEN;

    /**
     * 土规部参与项目整体提成比例
     */
    public static final BigDecimal PARTICIPANT_BROKERAGE_PERCENTAGE = new BigDecimal("0.5");

    /**
     * 土土规部非市场人员每月固定预提金额
     */
    public static final BigDecimal NON_MARKETER_MONTH_BROKERAGE = new BigDecimal("1000");

    /**
     * 营销主管培育人员数小于4人
     */
    public static final BigDecimal CULTIVATE_LOW_4 = new BigDecimal("0.015");

    /**
     * 营销主管培育人员数4到6人
     */
    public static final BigDecimal CULTIVATE_4_6 = new BigDecimal("0.0165");

    /**
     * 营销主管培育人员数大于6人
     */
    public static final BigDecimal CULTIVATE_HIGH_6 = new BigDecimal("0.018");

    /**
     * 市场部部门经理浮动提成系数
     */
    public static final BigDecimal MANAGER_FLOAT_RATIO_2 = new BigDecimal("0.2");

    /**
     * 市场部部门经理浮动提成系数
     */
    public static final BigDecimal MANAGER_FLOAT_RATIO_8 = new BigDecimal("0.8");


}
