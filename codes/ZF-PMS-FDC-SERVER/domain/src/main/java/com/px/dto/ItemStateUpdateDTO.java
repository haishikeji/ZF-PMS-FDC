package com.px.dto;

import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;
import lombok.Data;

import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/10/8
 * created with IntelliJ IDEA.
 */
@Data
public class ItemStateUpdateDTO {

    @AbcValidate(required = true,message = "项目列表id不能为空",fun = Check.NotNull)
    private List<Long> ids;

    private String state;
}
