package com.px.vo;

import com.px.bo.ReportNounBO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/8/29
 * created with IntelliJ IDEA.
 */
@Data
public class ReportMainVO {

    private Long id;

    private String placeholder;

    private String reportType;

    private Long  headlineRootId;

    private String reportKey;

    private String title;

    private String url;

    private String creator;

    private String checker;

    private String status;

    private LocalDateTime created;

    private String reportParams;

    private Map<String, List<ReportNounBO>> reportNouns;
}
