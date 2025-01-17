package com.px.dto;

import com.px.enums.ProductionEnum;
import lombok.Data;

import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/11/16
 * created with IntelliJ IDEA.
 */
@Data
public class PriceFeedbackDTO {

    private Long majorId;

    private String reportNo;

    private Integer printing;

    private String printingRemark;

    private List<ProductionEnum> production;

    /**
     * 针对价值意见书是否收款
     */
    private Boolean isPay;
}
