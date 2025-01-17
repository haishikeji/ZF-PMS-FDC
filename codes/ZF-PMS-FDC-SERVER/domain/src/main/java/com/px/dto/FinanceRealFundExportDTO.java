package com.px.dto;

import com.px.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class FinanceRealFundExportDTO {

    private Long id;

    /**
     * 付款方
     */
    @Excel(name = "付款方")
    private String payer;

    /**
     * 付款银行
     */
    @Excel(name = "付款银行")
    private String payerBank;

    /**
     * 付款金额
     */
    @Excel(name = "付款金额")
    private BigDecimal amount;

    /**
     * 到账时日
     */
    @Excel(name = "到账时日")
    private LocalDate payDatetime;

    @Excel(name= "已认领（元）")
    private BigDecimal claimAmount;

    @Excel(name= "未认领（元）")
    private BigDecimal notClaimAmount;

    @Excel(name= "录入人")
    private String creator;

    @Excel(name= "录入时间" ,dateFormat="yyyy-MM-dd")
    private Date created;

    @Excel(name = "备注")
    private String remark;




}
