package com.px.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.px.annotation.*;
import com.px.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.px.enums.MainBusinessEnum.PERSONAL_BUSINESS;

/**
 * <p>
 * 个贷业务订单
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "个贷业务订单")
@LuceneResource(value = PERSONAL_BUSINESS,serviceImplClassName = "personalServiceImpl",
        url = "/personal/pending/list",menuName = "正在进行",checkField="ifSaveFile")
public class Personal extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 订单编号
     */
    @ImportCell
    @ExportCell(columnName = "订单编号")
    @LuceneSearchable("order_id")
    @Excel(name = "订单编号")
    private String orderId;

    /**
     * 坐落
     */
    @ImportCell
    @ExportCell(columnName = "坐落")
    @LuceneSearchable
    @Excel(name = "坐落")
    private String location;

    /**
     * 评估目的
     */
    @ImportCell
    @ExportCell(columnName = "评估目的")
    private String evaluateAim;

    /**
     * 房屋用途
     */
    @ImportCell
    @ExportCell(columnName = "房屋用途")
    private String purpose;

    /**
     * 客户类型（企业，个人）
     */
    @ImportCell
    @ExportCell(columnName = "客户类型（企业，个人）")
    private String clienteleType;

    /**
     * 客户id
     */
    private Long clienteleId;

    /**
     * 业务来源（下级客户id）
     */
    private Long clienteleSubId;

    /**
     * 客户联系人id
     */
    private Long clienteleContactId;

    /**
     * 中介
     */
    @ImportCell
    @ExportCell(columnName = "中介")
    @Excel(name = "中介")
    private String agent;

    /**
     * 贷款性质
     */
    @ImportCell
    @ExportCell(columnName = "贷款性质")
    private String loanNature;

    /**
     * 贷款用途
     */
    @ImportCell
    @ExportCell(columnName = "贷款用途")
    private String loanAim;

    /**
     * 贷款额度
     */
    @ImportCell
    @ExportCell(columnName = "贷款额度")
    private BigDecimal loanLimit;

    /**
     * 贷款期限
     */
    @ImportCell
    @ExportCell(columnName = "贷款期限")
    private BigDecimal loanPeriod;

    /**
     * 贷款是否到期
     */
    @ImportCell
    @ExportCell(columnName = "贷款是否到期")
    private Boolean loanExpire;

    /**
     * 委托人1
     */
    @TableField("bailorA")
    @ImportCell
    @ExportCell(columnName = "委托人1")
    @LuceneSearchable("bailorA")
    @Excel(name = "委托人1")
    private String bailorA;

    /**
     * 委托人2
     */
    @TableField("bailorB")
    @ImportCell
    @ExportCell(columnName = "委托人2")
    @LuceneSearchable("bailorB")
    @Excel(name = "委托人2")
    private String bailorB;

    /**
     * 委托人1电话
     */
    @TableField("bailorA_tel")
    @ImportCell
    @ExportCell(columnName = "委托人1电话")
    @LuceneSearchable("bailorA_tel")
    @Excel(name = "委托人1电话")
    private String bailoraTel;

    /**
     * 委托人2电话
     */
    @TableField("bailorB_tel")
    @ImportCell
    @ExportCell(columnName = "委托人2电话")
    @LuceneSearchable("bailorB_tel")
    @Excel(name = "委托人2电话")
    private String bailorbTel;

    /**
     * 终端客户类型（企业，个人）
     */
    @ImportCell
    @ExportCell(columnName = "终端客户类型（企业，个人）")
    private String terminalClienteleType;

    /**
     * 终端联系人
     */
    private Long terminalClienteleContactId;

    /**
     * 终端客户id
     */
    private Long terminalClienteleId;

    /**
     * 资料说明
     */
    @ImportCell
    @ExportCell(columnName = "资料说明")
    private String credentials;

    /**
     * 客户经理id(user_id)
     */
    private Long clientManagerId;

    /**
     * 外业人员
     */
    private Long outwardStaff;

    /**
     * 内页人员
     */
    private Long inwardStaff;

    /**
     * 定价人员
     */
    private Long pricingStaff;

    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String remark;

    /**
     * 归档审核
     */
    @ImportCell
    @ExportCell(columnName = "归档审核")
    private Boolean saveFileCheck;

    /**
     * 是否归档
     */
    @ImportCell
    @ExportCell(columnName = "是否归档")
    @LuceneSearchable(value="if_save_file",url = "/personal/saveFile",menuName = "已归档项目")
    private Boolean ifSaveFile;

    /**
     * 归档日期
     */
    @ImportCell
    @ExportCell(columnName = "归档日期")
    private Date saveFileDate;

}
