package com.px.brokerage.factory;

import com.px.brokerage.BrokerageCalculator;
import com.px.brokerage.constants.BrokerageRule;

/**
 * 类说明：提成计算器抽象工厂
 *
 * @author: wucl
 * @since: 2023/3/6
 * created with IntelliJ IDEA.
 */
public interface BrokerageCalculateFactory {

    BrokerageCalculator getCalculator(BrokerageRule rule);
}
