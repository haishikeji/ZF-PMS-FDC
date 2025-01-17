package com.px.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/6/7
 * created with IntelliJ IDEA.
 */
@Data
public class DeductionDTO {

    private Long sequenceId;

    private BigDecimal totalAmount;

    private BigDecimal notDeduAmount;
}
