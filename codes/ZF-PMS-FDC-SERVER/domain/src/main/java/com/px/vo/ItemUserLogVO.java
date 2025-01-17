package com.px.vo;

import com.px.entity.ItemUserLog;
import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2022/12/8
 * created with IntelliJ IDEA.
 */
@Data
public class ItemUserLogVO extends ItemUserLog {

    private String itemName;

    private String userName;

    private String stageName;
}
