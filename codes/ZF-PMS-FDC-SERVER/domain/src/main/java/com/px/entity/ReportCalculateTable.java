package com.px.entity;

import com.px.common.BaseEntity;
import lombok.Data;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/9/19
 * created with IntelliJ IDEA.
 */
@Data
public class ReportCalculateTable  extends BaseEntity {

    /**
     *标题id
     */
    private Long headlineId;
    /**
     *表名称
     */
    private String tableName;
    /**
     *编号
     */
    private String tableNo;
    /**
     *表映射的java类全限定名
     */
    private String tableClass;

}
