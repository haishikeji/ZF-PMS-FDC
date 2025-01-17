package com.px.dto;

import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;
import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2022/12/27
 * created with IntelliJ IDEA.
 */
@Data
public class UpdatePasswordDTO {

    @AbcValidate(required = true,message = "原密码不能为空", fun = Check.NotEmpty)
    private String originalPassword;

    @AbcValidate(required = true,message = "新密码不能为空", fun = Check.NotEmpty)
    private String currentPassword;

    @AbcValidate(required = true,message = "二次新密码不能为空", fun = Check.NotEmpty)
    private String repeatPassword;
}
