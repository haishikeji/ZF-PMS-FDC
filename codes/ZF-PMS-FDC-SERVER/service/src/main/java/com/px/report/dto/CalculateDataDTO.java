package com.px.report.dto;

import lombok.Data;

import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/9/18
 * created with IntelliJ IDEA.
 */
@Data
public class CalculateDataDTO {

    private Long sectionId;

    private List<TableData> tableData;

    private List<Params> params;

}
