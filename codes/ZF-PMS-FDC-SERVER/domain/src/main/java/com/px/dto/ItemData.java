package com.px.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2024/3/7
 * created with IntelliJ IDEA.
 */
@Data
public class ItemData {
    private String valueA;

    private String valueB;

    private BigDecimal weight;
}
