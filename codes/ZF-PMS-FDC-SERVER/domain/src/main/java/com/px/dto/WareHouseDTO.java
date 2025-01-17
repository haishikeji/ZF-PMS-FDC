package com.px.dto;

import lombok.Builder;
import lombok.Data;


/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/12/7
 * created with IntelliJ IDEA.
 */
@Builder
@Data
public class WareHouseDTO  {

    private String reportNo;

    private Boolean repertoryState;

    private String productionType;

    private Long productionId;
}
