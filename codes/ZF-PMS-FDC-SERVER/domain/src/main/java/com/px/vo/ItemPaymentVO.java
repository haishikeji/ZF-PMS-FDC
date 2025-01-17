package com.px.vo;

import com.px.entity.Item;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/1/11
 * created with IntelliJ IDEA.
 */
@Data
public class ItemPaymentVO extends Item {

    /**
     * 已回款
     */
    private BigDecimal payedAmount;

    /**
     * 未回款
     */
    private BigDecimal NotPayedAmount;
}
