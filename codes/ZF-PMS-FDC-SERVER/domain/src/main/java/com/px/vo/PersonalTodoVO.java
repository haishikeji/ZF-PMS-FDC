package com.px.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类说明：个贷待办列表VO
 *
 * @author: wucl
 * @since: 2024/4/8
 * created with IntelliJ IDEA.
 */
@Data
public class PersonalTodoVO {

    private Long id;

    private Long taskId;

    private Long currentNodeId;

    private Long businessId;

    private String nodeName;

    private String nodeCode;

    private Long handlerId;

    private String location;

    private String orderId;

    private String clientManager;

    private String handlerName;

    private Boolean taskFinished;

    private Date created;

    private String clientName;

    private String clientSubName;

    private String agent;

    private String contactName;

    private String contactTel;

    private String bailorA;

    private String bailorB;

    private String bailorATel;

    private String bailorBTel;

    private String outwardStaffName;

    private String inwardStaffName;

    private String pricingStaffName;

    private Date nodeCreated;

    private BigDecimal acreage;

    private BigDecimal amount;

    private BigDecimal price;

    private Date instanceCreated;

    private String startDate;

    private String endDate;

    private Boolean isOnline;

    private String comments;

    private Boolean getNodeIsBookbindingStamp;

}
