package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

@Data
public class MajorErrorTypeCountVO {

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String userName;

    /**
     * 委托书界定事项和报告描述不符
     */
    @Excel(name = "委托书界定事项和报告描述不符")
    private Integer errorTypeCount01;

    /**
     * 委托方函重要数据出错
     */
    @Excel(name = "委托方函重要数据出错")
    private Integer errorTypeCount02;

    /**
     * 假设限制条件错漏
     */
    @Excel(name = "假设限制条件错漏")
    private Integer errorTypeCount03;

    /**
     * 地价定义表述有误或不完整
     */
    @Excel(name = "地价定义表述有误或不完整")
    private Integer errorTypeCount04;

    /**
     * 大小写有误
     */
    @Excel(name = "大小写有误")
    private Integer errorTypeCount05;

    /**
     * 面积有误
     */
    @Excel(name = "面积有误")
    private Integer errorTypeCount06;

    /**
     * 报告章节缺漏
     */
    @Excel(name = "报告章节缺漏")
    private Integer errorTypeCount07;

    /**
     * 技术路线错误
     */
    @Excel(name = "技术路线错误")
    private Integer errorTypeCount08;

    /**
     * 测算参数取值错误
     */
    @Excel(name = "测算参数取值错误")
    private Integer errorTypeCount09;

    /**
     *
     */
    @Excel(name = "实景图方位图错误")
    private Integer errorTypeCount10;

    /**
     * 报告出具日期错误
     */
    @Excel(name = "报告出具日期错误")
    private Integer errorTypeCount11;

    /**
     * 报告有效期错误
     */
    @Excel(name = "报告有效期错误")
    private Integer errorTypeCount12;

    /**
     * 估价依据适用不当
     */
    @Excel(name = "估价依据适用不当")
    private Integer errorTypeCount13;

    /**
     * 委托人名称出错
     */
    @Excel(name = "委托人名称出错")
    private Integer errorTypeCount14;

    /**
     * 价值时点出错
     */
    @Excel(name = "价值时点出错")
    private Integer errorTypeCount15;


}
