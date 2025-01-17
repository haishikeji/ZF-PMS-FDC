package com.px.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.px.common.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;

/**
 * <p>
 * 项目提成总表
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "项目提成总表")
@Builder
public class ItemBrokerageGeneral extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 项目id
     */
    private Long itemId;

    /**
     * 客户经理提成金额
     */
    private BigDecimal marketerBrokerageAmount;

    /**
     * 客户经理预提
     */
    private Boolean marketerAdvanceBrokerageAble;

    /**
     * 客户经理已预提
     */
    private Boolean marketerAdvanceBrokerageDone;

    /**
     * 参与人预计提成总金额
     */
    @TableField("participant_predict_brokerage_amount")
    private BigDecimal participantPredictBrokerageAmount;

    /**
     * 参与人实际提成总金额
     */
    @TableField("participant_actual_brokerage_amount")
    private BigDecimal participantActualBrokerageAmount;

    /**
     * 参与人预提中
     */
    private Boolean participantAdvanceStatus;

    /**
     * 预计提成总金额
     */
    private BigDecimal brokeragePredictAmount;

    /**
     * 已提成总金额
     */
    private BigDecimal brokerageActualAmount;

    /**
     * 提成率
     */
    private BigDecimal brokerageRate;

    /**
     * 参与人提成状态
     */
    private String participantStatus;

    /**
     * 客户经理提成状态
     */
    private String marketerStatus;

}
