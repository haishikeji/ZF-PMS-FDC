package com.px.vo;

import com.px.annotation.Excel;
import com.px.entity.Assets;
import lombok.Data;

@Data
public class AssetsVO extends Assets {

    /**
     * 资产业务id
     */
    private Long assetsId;

    /**
     * 意见书号
     */
    @Excel(name = "意见书号")
    private String statementNo;

    /**
     * 报告号
     */
    @Excel(name = "报告号")
    private String reportNo;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;

    /**
     * 业务来源(客户子级名称)
     */
    @Excel(name = "业务来源")
    private String customerSubName;

    /**
     *  当前实例节点id
     */
    private Long currentNodeId;

    /**
     * 当前实例节点名称
     */
    @Excel(name = "当前节点")
    private String currentNodeName;

    /**
     * 当前实例节点编码
     */
    private String currentNodeCode;

    /**
     * 当前节点处理人id
     */
    private Long handlerId;

    /**
     * 当前节点处理人
     */
    @Excel(name = "当前节点处理人")
    private String handlerName;

    /**
     * 项目负责人
     */
    @Excel(name = "项目负责人")
    private String principalName;

    /**
     * 客户经理
     */
    @Excel(name = "客户经理")
    private String clientManagerName;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 评估结论对应的评估值(万)
     */
    @Excel(name = "评估结论对应的评估值(万)")
    private Double estimatedValue;

    /**
     * 订单应收款(元)
     */
    @Excel(name = "订单应收款(元)")
    private Double shouldAmount;

    /**
     * 订单实收款(元)
     */
    @Excel(name = "订单实收款(元)")
    private Double realAmount;

    /**
     * 标准收费(元)
     */
    @Excel(name = "标准收费(元)")
    private Double standardAmount;

    /**
     * 折扣比例
     */
    @Excel(name = "折扣比例")
    private Double discount;

    /**
     * 实际开票金额
     */
    @Excel(name = "实际开票金额")
    private Double invoiceRealAmount;

    /**
     * 客户联系人名
     */
    private String linkmanName;

    /**
     * 客户联系人部门
     */
    private String linkmanDepartment;

    /**
     * 客户联系人岗位
     */
    private String linkmanDuty;

    /**
     * 客户联系人联系方式
     */
    private String linkmanMobile;

    /**
     * 终端客户联系人id
     */
    private Long tLinkmanId;

    /**
     * 终端客户联系人名
     */
    private String tLinkmanName;

    /**
     * 终端客户联系人联系方式
     */
    private String tLinkmanMobile;

    /**
     * 项目参与人(json数组)
     */
    private String members;

    /**
     * 产品类型
     */
    private String productionType;

    /**
     * 订单收款id
     */
    private Long orderFundId;

    /**
     * 任务id
     */
    private Long recordId;

    /**
     * 报告出库申请id
     */
    private Long warehouseId;

    /**
     * 产品应收款
     */
    @Excel(name = "产品应收款")
    private Double productionShouldAmount;

    /**
     * 产品实收款
     */
    @Excel(name = "产品实收款")
    private Double productionRealAmount;

    /**
     * 产品收款信息id
     */
    private Long productionFundId;

    /**
     * 审核意见
     */
    private String comment;

    /**
     * 报告库存状态
     */
    private Boolean reportRepertoryState;

    /**
     * 报告送达状态
     */
    @Excel(name = "报告送达状态",readConverterExp=("true=已送达,false=未送达"))
    private Boolean reportDelivery;

    /**
     * 意见书项目名称
     */
    private String statementName;

    /**
     * 报告项目名称
     */
    private String reportName;
}
