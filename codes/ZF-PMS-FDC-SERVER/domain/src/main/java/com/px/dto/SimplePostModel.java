package com.px.dto;

import com.px.common.SimpleListModel;
import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/2/27
 * created with IntelliJ IDEA.
 */
@Data
public class SimplePostModel extends SimpleListModel {

    private Long departmentId;

    private Long parentId;

}
