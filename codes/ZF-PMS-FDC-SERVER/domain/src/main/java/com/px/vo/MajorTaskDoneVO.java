package com.px.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 类说明：大中型项目我的已办列表
 *
 * @author: wucl
 * @since: 2023/11/30
 * created with IntelliJ IDEA.
 */
@Data
public class MajorTaskDoneVO {

    /**
     * id
     */
    private Long id;
    /**
     * 订单id
     */
    private Long majorId;

    /**
     * 处理人id
     */
    private Long handlerId;

    /**
     * 处理人
     */
    private String handler;


    private String state;


    /**
     * 完成时间
     */
    private LocalDate finishTime;

    /**
     * 完成时长
     */
    private String handingTime;

    private Long  handingTimeLong;

    /**
     * 处理意见
     */
    private String comments;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 市场订单号
     */
    private String orderId;


    /**
     * 订单名称
     */
    private String majorName;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 报告号
     */
    private String reportNo;

    private String startDate;

    private String endDate;

    private String instanceState;

    private Long instanceId;

    private String productionName;

    private String productionType;

    private String statementNo;

}
