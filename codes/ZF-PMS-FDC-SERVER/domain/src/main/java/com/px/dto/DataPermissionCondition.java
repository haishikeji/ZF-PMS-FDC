package com.px.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/2/16
 * created with IntelliJ IDEA.
 */
@Data
public class DataPermissionCondition {

    private Set<Long> userIds;

    private LocalDate startDate;
    private LocalDate endDate;

    private String keyword;

}
