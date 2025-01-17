package com.px.report.calculation.cityPrice.bo;

import com.px.report.calculation.cityPrice.annotation.TableHead;
import com.px.report.html.DataToHtmlBO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类说明：城区商服中心各评定指标归一化值及综合指标值表
 *
 * @author: wucl
 * @since: 2023/9/14
 * created with IntelliJ IDEA.
 */
@Builder
@Data
public class BusinessCenterFormatBO extends DataToHtmlBO {

    /**
     * 编号
     */
    @TableHead(name="编号")
    private Integer no;
    /**
     * 商服名称
     */
    @TableHead(name="商服名称")
    private String name;
    /**
     * 土地面积归一
     */
    @TableHead(name="土地面积归一")
    private BigDecimal acreage;
    /**
     * 店铺数量归一
     */
    @TableHead(name="店铺数量归一")
    private BigDecimal shopNum;
    /**
     * 购物人次归一
     */
    @TableHead(name="购物人次归一")
    private BigDecimal shoppingNum;
    /**
     * 营业额归一
     */
    @TableHead(name="营业额归一")
    private BigDecimal turnover;
    /**
     * 平均租金归一
     */
    @TableHead(name="平均租金归一")
    private BigDecimal rent;
    /**
     * 综合指标值
     */
    @TableHead(name="综合指标值")
    private BigDecimal comprehensive;
}
