package com.px.entity;
import com.px.annotation.Excel;
import com.px.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ImportCell;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "客户表")
public class Customer extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 客户名称
     */
    @ImportCell(notNull = true)
    @Excel(name = "客户名称")
    private String name;

    /**
     * 省市县
     */
    @ImportCell
    private String city;

    /**
     * 区/县 （已弃用）
     */
    @ImportCell
    private String county;

    /**
     * 部门
     */
    @ImportCell
    @Excel(name = "部门")
    private String department;

    /**
     * 职位
     */
    @ImportCell
    @Excel(name = "职位")
    private String position;

    /**
     * 科室
     */
    @ImportCell
    @Excel(name = "科室")
    private String section;

    /**
     * 联系电话
     */
    @ImportCell
    @Excel(name = "联系电话")
    private String mobile;

    /**
     * 微信号
     */
    @ImportCell
    @Excel(name = "微信号")
    private String wechatNo;

    /**
     * QQ号
     */
    @TableField("QQ")
    @ImportCell
    @Excel(name = "QQ号")
    private String qq;

    /**
     * 客户等级
     */
    @ImportCell()
    @Excel(name = "客户等级")
    private String level;

    /**
     * 客户地址
     */
    @ImportCell
    @Excel(name = "客户地址")
    private String address;

    /**
     * 所属员工id
     */
    @ImportCell
    private Long userId;

}
