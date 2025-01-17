package com.px.dto;

import com.px.annotation.ImportCell;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2024/4/10
 * created with IntelliJ IDEA.
 */
@Data
public class PersonalClaimDTO {

    @ImportCell(notNull = true)
    @NotEmpty(message = "项目编号为空")
    private String orderId;

    @ImportCell(notNull = true)
    @NotEmpty(message = "认领金额为空")
    private BigDecimal amount;
}
