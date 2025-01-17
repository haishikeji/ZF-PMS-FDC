package com.px.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 类说明：大中型产品归档VO
 *
 * @author: wucl
 * @since: 2023/11/30
 * created with IntelliJ IDEA.
 */
@Data
public class MajorProductionSaveDoneVO {

    private Long id;
    /**
     * 业务主id
     */
    private Long majorId;

    /**
     * 项目名称
     */
    private String name;
    /**
     * 订单名称
     */
    private String orderName;
    /**
     * 报告号
     */
    private String reportNo;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 客户经理
     */
    private String clientManager;

    /**
     * 项目负责人
     */
    private String principal;

    /**
     * 评估对象类型
     */
    private String businessObjectType;

    /**
     * 产品类型
     */
    private String production;

    /**
     *库存状态（0：已入库  1：已出库）
     */
    private Boolean repertoryState;

    /**
     * 归档日期
     */
    private LocalDate saveFileDate;

    /**
     * 实收款
     */
    private BigDecimal realAmount;

    private String startDate;

    private String endDate;
}
