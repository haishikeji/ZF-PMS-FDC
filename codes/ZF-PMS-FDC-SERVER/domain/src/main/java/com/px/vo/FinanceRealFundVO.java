package com.px.vo;

import com.px.entity.FinanceRealFund;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/12/11
 * created with IntelliJ IDEA.
 */
@Data
public class FinanceRealFundVO extends FinanceRealFund {

    private String creator;

    private BigDecimal claimAmount;

    private BigDecimal notClaimAmount;

    private List<String> recommends;

    private Long userId;
}
