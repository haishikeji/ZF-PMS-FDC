package com.px.vo;

import com.px.entity.Post;
import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2022/12/5
 * created with IntelliJ IDEA.
 */
@Data
public class PostVO extends Post {
    private String departmentName;
    private String leaderDepartmentName;
    private String leaderPost;
    private Long leaderDepartmentId;
}
