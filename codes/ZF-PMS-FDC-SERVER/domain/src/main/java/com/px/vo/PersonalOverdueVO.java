package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class PersonalOverdueVO{

    private Long id;

    @Excel(name = "超期天数")
    private Integer overdueDay;

    @Excel(name = "项目编号")
    private String orderId;

    @Excel(name = "坐落")
    private String location;

    @Excel(name = "面积")
    private BigDecimal acreage;

    @Excel(name = "单机")
    private BigDecimal price;

    @Excel(name = "总价")
    private BigDecimal amount;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "业务来源")
    private String clientSubName;

    @Excel(name = "中介")
    private String agent;

    @Excel(name = "联系人")
    private String contactName;

    @Excel(name = "联系电话")
    private String contactTel;

    @Excel(name = "委托人一")
    private String bailorA;

    @Excel(name = "委托人二")
    private String bailorB;

    @Excel(name = "客户经理")
    private String clientManager;

    @Excel(name = "勘察人")
    private String outwardStaffName;

    @Excel(name = "产品内业")
    private String inwardStaffName;

    @Excel(name = "定价内业")
    private String pricingStaffName;

    @Excel(name = "出库日期")
    private Date rot;

    private Integer overdueMin;

    private Integer overdueMax;

    /**
     * 人员id列表
     */
    private Set<Long> userIds;


}
