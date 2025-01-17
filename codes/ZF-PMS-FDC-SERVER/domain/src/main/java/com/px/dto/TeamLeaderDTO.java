package com.px.dto;

import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/4/10
 * created with IntelliJ IDEA.
 */
@Data
public class TeamLeaderDTO {

    private Long supervisorId;

    private Long sPostId;

    private Long cultivateId;

    private Long cPostId;
}
