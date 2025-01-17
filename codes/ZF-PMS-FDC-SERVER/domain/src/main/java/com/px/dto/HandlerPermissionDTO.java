package com.px.dto;

import com.px.enums.workflow.HandlerPermissionEnum;
import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/10/24
 * created with IntelliJ IDEA.
 */
@Data
public class HandlerPermissionDTO {

    private HandlerPermissionEnum name;

    private Long powerId;
}
