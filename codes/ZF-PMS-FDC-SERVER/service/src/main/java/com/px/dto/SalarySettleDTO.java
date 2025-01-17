package com.px.dto;

import lombok.Builder;

import java.time.LocalDate;

/**
 * 类说明：薪水结算DTO
 *
 * @author: wucl
 * @since: 2023/3/13
 * created with IntelliJ IDEA.
 */
@Builder
public class SalarySettleDTO {

    private LocalDate startDate;

    private LocalDate endDate;

    private Long userId;

    private Integer month;

}
