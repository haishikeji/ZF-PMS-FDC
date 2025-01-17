package com.px.vo;


import com.px.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class RealFundPersonalVO {

    private Long id;

    @Excel(name = "归档状况",readConverterExp="true=已归档,false=未归档")
    private Boolean ifSaveFile;

    @Excel(name = "项目编号")
    private String orderId;

    @Excel(name = "项目名称")
    private String location;

    @Excel(name = "所属部门")
    private String marketDepartment;

    @Excel(name = "客户经理")
    private String clientManager;

    @Excel(name = "入账日期")
    private LocalDate payDatetime;

    @Excel(name = "认领日期")
    private LocalDate claimDatetime;

    @Excel(name = "实收款金额")
    private BigDecimal claimAmount;

    @Excel(name = "认领人")
    private String claimUser;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "业务来源")
    private String clientSubName;

    @Excel(name = "委托人")
    private String bailorA;

    @Excel(name = "评估价值(万)")
    private BigDecimal amount;

    /**
     * 筛选开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 筛选结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 筛选开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payStartTime;

    /**
     * 筛选结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payEndTime;

}
