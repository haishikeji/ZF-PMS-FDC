package com.px.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RealFundAssetsStatDTO {

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
     * 项目编号
     */
    private String orderId;

    /**
     * 报告号
     */
    private String reportNo;

    /**
     * 项目负责人id(user_id)
     */
    private Long principalId;

    /**
     * 认领人id(user_id)
     */
    private Long claimUserId;

    /**
     * 认领金额
     */
    private BigDecimal claimAmount;

    /**
     * 资产业务类型
     */
    private String assetsBusinessGener;

    /**
     * 是否归档(true=已归档,false=未归档)
     */
    private Boolean ifSaveFile;

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
