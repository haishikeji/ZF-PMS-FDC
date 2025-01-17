package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/12/8
 * created with IntelliJ IDEA.
 */
@Data
public class MajorOrderVO {

    /**
     * id
     */
    private Long id;

    /**
     * 订单id
     */
    private Long businessId;

    /**
     * 订单收款id
     */
    private Long orderFundId;

    /**
     * 产品收款id
     */
    private Long productionFundId;

    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderId;

    /**
     * 订单名称
     */
    @Excel(name = "订单名称")
    private String orderName;

    /**
     * 产品号
     */
    @Excel(name = "产品号")
    private String productionNo;

    /**
     * 产品类型
     */
    @Excel(name = "产品类型")
    private String production;

    /**
     * 当前节点
     */
    @Excel(name = "当前节点")
    private String currentNodeName;

    /**
     *负责人
     */
    @Excel(name = "负责人")
    private String principal;

    /**
     *客户经理
     */
    @Excel(name = "客户经理")
    private String clientManager;

    /**
     *应收款
     */
    @Excel(name = "应收款")
    private BigDecimal shouldAmount;

    /**
     *实收款
     */
    @Excel(name = "实收款")
    private BigDecimal realAmount;

    /**
     *开票金额
     */
    @Excel(name = "开票金额")
    private BigDecimal invoiceAmount;

    /**
     *客户名称
     */
    @Excel(name = "客户名称")
    private String clienteleName;

    /**
     *业务来源
     */
    @Excel(name = "业务来源")
    private String clienteleSubName;

    /**
     *客户联系人
     */
    @Excel(name = "客户联系人")
    private String clienteleContactName;

    /**
     *委托人
     */
    @Excel(name = "委托人")
    private String bailor;

    /**
     *产权人
     */
    @Excel(name = "产权人")
    private String owner;

    /**
     * 送达状态
     */
    @Excel(name = "送达状态",readConverterExp=("true=已送达,false=未送达"))
    private Boolean delivery;

    /**
     * 客户经理USER_ID
     */
    private Long clientManagerId;

    /**
     *订单应收款
     */
    private BigDecimal orderShouldAmount;

    /**
     *订单实收款
     */
    private BigDecimal orderRealAmount;

    /**
     * 下单时间
     */
    @Excel(name = "下单时间")
    private Date created;

    /**
     * 应收款备注
     */
    private String remark;

    private String startDate;

    private String endDate;

    /**
     * 市场人员申报提成
     */
    private String mDeclareResult;

    /**
     * 评估人员申报提成
     */
    private String eDeclareResult;

    private String name;

    /**
     * 项目负责人id(user_id)
     */
    private Long principalId;

    private Boolean ifSaveFile;

    private BigDecimal evaluateAmount;

    private BigDecimal planAmount;

    /**
     * 是否收款
     */
    private Boolean hasRealAmount;

    /**
     * 是否有应收款
     */
    private Boolean hasShouldAmount;

}
