package com.px.report.calculation.cityPrice.bo;

import com.px.report.calculation.cityPrice.annotation.TableHead;
import com.px.report.html.DataToHtmlBO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类说明：城区商服中心规模指数表
 *
 * @author: wucl
 * @since: 2023/9/14
 * created with IntelliJ IDEA.
 */
@Builder
@Data
public class BusinessCenterScaleBO extends DataToHtmlBO {

    /**
     *  级别
     */
    @TableHead(name="级别")
    private String level;
    /**
     *  综合指标平均值
     */
    @TableHead(name="综合指标平均值")
    private BigDecimal avgComprehensive;
    /**
     * 规模指数
     */
    @TableHead(name="规模指数")
    private BigDecimal scaleOrigin;
    /**
     * 调整后规模指数
     */
    @TableHead(name="调整后规模指数")
    private BigDecimal scaleValue;

}
