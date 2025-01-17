package com.px.service;

import com.px.dto.MarketStatDTO;
import com.px.vo.MarketStatVO;

import java.math.BigDecimal;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/1/13
 * created with IntelliJ IDEA.
 */
public interface IMarketStatService {
    MarketStatVO marketStat(MarketStatDTO dto);

    BigDecimal currentMonthPayment(Long userId);

    BigDecimal currentYearPayment(Long userId);
    BigDecimal currentYearTeamPayment(Long userId);
}
