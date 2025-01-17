package com.px.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/6/9
 * created with IntelliJ IDEA.
 */
@Data
public class TechDeductionVO {

    /**
     * 结算id
     */
    private Long id;

    /**
     * 技术人员id
     */
    private Long userId;

    /**
     * 技术人员名字
     */
    private String userName;
    /**
     * 结算年份
     */
    private Long year;
    /**
     * 结算月份
     */
    private Long month;

    /**
     * 已发金额
     */
    private BigDecimal brokerageAmount;

    /**
     * 结算类型
     */
    private String cate;
    /**
     *结算时间
     */
    private LocalDate created;
    /**
     * 未抵扣金额
     */
    private BigDecimal notDeductionAmount;
    /**
     * 抵扣记录
     */
    private List<DeductionVO> deductionRecords = new ArrayList<>();

}
