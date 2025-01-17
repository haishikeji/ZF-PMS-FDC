package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class AssetsProdOverdueVO {

    @Excel(name = "超期天数")
    private Integer overdueDay;

    @Excel(name = "分单类型")
    private String allotType;

    @Excel(name = "项目编号")
    private String orderId;

    @Excel(name = "产品号")
    private String productionNo;

    @Excel(name = "产品类型")
    private String productionType;

    @Excel(name = "项目名称")
    private String projectName;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "业务来源")
    private String clientSubName;

    @Excel(name = "资产业务类型")
    private String assetsBusinessGener;

    @Excel(name = "客户经理")
    private String clientManager;

    @Excel(name = "负责人")
    private String principal;

    @Excel(name = "评估价值")
    private BigDecimal evaluateAmount;

    @Excel(name = "订单应收款")
    private BigDecimal shouldAmount;

    @Excel(name = "标准收费")
    private BigDecimal standardAmount;

    @Excel(name = "出库时间" ,dateFormat="yyyy-MM-dd HH:mm:ss")
    private Date repertoryOutTime;


    private Integer overdueMin;

    private Integer overdueMax;

    /**
     * 人员id列表
     */
    private Set<Long> userIds;
}
