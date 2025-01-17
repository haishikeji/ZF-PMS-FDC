package com.px.condition;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class LoginCondition {

    /**
     * 账号（教职工账号为职工编号）
     */
    @NotEmpty(message = "账号不能为空")
    private String account;
    /**
     * 密码UserServiceImpl
     */
    @NotEmpty(message = "密码不能为空")
    private String pwd;


}
