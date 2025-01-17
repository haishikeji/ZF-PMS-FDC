package com.px.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.px.annotation.*;
import com.px.common.BaseEntity;
import java.time.LocalDate;

import com.px.enums.SexEnum;
import com.px.enums.UserTypeEnum;
import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 基础用户
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "基础用户")
public class User extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 姓名
     */
    @ImportCell(notNull = true)
    @ExportCell(columnName = "姓名")
    private String name;

    /**
     * 身份证号
     */
    @ImportCell(notNull = true)
    @ExportCell(columnName = "身份证号")
    private String idNo;


    /**
     * 手机号码
     */
    @ImportCell(notNull = true)
    @ExportCell(columnName = "手机号码")
    private String mobile;



    /**
     * 用户性别 男/女
     */
    @ImportCell(ref = SexEnum.class,notNull = true)
    @ExportCell(columnName = "用户性别 男/女" )
    private String sex;

    /**
     * 员工类型
     */
    @ImportCell(notNull = true,ref = UserTypeEnum.class)
    @ExportCell(columnName = "员工类型")
    private String userType;

    /**
     * 出生日期
     */
    @ImportCell(notNull = true)
    @ExportCell(columnName = "出生日期")
    private LocalDate birthday;


    /**
     * 民族
     */
    @ImportCell
    @ExportCell(columnName = "民族")
    private String nation;

    /**
     * 政治面貌
     */
    @ImportCell
    @ExportCell(columnName = "政治面貌")
    private String politics;

    /**
     * 籍贯
     */
    @ImportCell
    @ExportCell(columnName = "籍贯")
    private String nativePlace;

    /**
     * 家庭住址
     */
    @ImportCell
    @ExportCell(columnName = "家庭住址")
    private String homeAddress;

    /**
     * 入职日期
     */
    @ImportCell(notNull = true)
    @ExportCell(columnName = "入职日期")
    @TableField(fill = FieldFill.UPDATE)
    @AbcValidate(required = true,message = "入职日期必填", fun = Check.NotNull)
    private LocalDate hireDate;


    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String remark;

    /**
     * 密码
     */
    //@ImportCell
    @ExportCell(columnName = "密码")
    private String password;

    /**
     * 盐加密
     */
    //@ImportCell
    @ExportCell(columnName = "盐加密")
    private String salt;



    /**
     * 是否启用：1：启用 0：禁用
     */
    //@ImportCell
    @ExportCell(columnName = "是否启用：1：启用 0：禁用")
    private Boolean enable;



    /**
     * 离职日期
     */
    //@ImportCell
    @ExportCell(columnName = "离职日期")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDate resignDate;

    /**
     * 员工工号（登录账号）
     */
    //@ImportCell
    @ExportCell(columnName = "员工工号")
    private String staffNo;

    /**
     * 在职状态
     */
    //@ImportCell
    @ExportCell(columnName = "在职状态：0：在职 1：离职")
    private Boolean resignState;

    /**
     * 初始密码状态
     */
    //@ImportCell
    @ExportCell(columnName = "初始密码状态")
    private Boolean passwordState;

    /**
     * 查询提成密码
     */
    private String settlePassword;
}
