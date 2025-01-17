package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SettleAssetsProductionVO {

    private Long id;

    @Excel(name = "项目编号")
    private String orderId;

    @Excel(name = "产品号", width = 30)
    private String productionNo;

    @Excel(name = "结算日期")
    private LocalDate settleDate;

    @Excel(name = "认领日期")
    private LocalDate claimDate;

    @Excel(name = "归档日期")
    private LocalDate saveFileDate;

    @Excel(name = "送达日期")
    private LocalDate deliveryDate;

    @Excel(name = "到账日期")
    private LocalDate payDateTime;

    @Excel(name = "项目名称", width = 30)
    private String projectName;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "业务来源")
    private String clientSubName;

    @Excel(name = "委托方")
    private String bailor;

    @Excel(name = "市场部")
    private String marketDepartmentName;

    @Excel(name = "评估部")
    private String evaluateDepartmentName;

    @Excel(name = "客户经理")
    private String clientManager;

    @Excel(name = "负责人")
    private String principal;

    @Excel(name = "结算金额(元)")
    private BigDecimal claimAmount;

    @Excel(name = "评估价值(万元)")
    private BigDecimal evaluateAmount;

    /**
     * 开票信息
     */
    private List<SettleProInvoiceVo> invoices;

    private String startDate;

    private String endDate;

    private String payStartDate;

    private String payEndDate;

}
