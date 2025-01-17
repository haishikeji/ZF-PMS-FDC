package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

@Data
public class PersonalOutwardEfficiencyVO {

    /**
     * 外业人员id
     */
    private Integer outwardStaff;

    /**
     * 外业人员姓名
     */
    @Excel(name = "外业人员姓名")
    private String userName;

    /**
     * 看件订单数
     */
    @Excel(name = "看件订单数")
    private Integer orderCount;

    /**
     * 看件房屋数
     */
    @Excel(name = "看件房屋数")
    private Integer houseCount;

}
