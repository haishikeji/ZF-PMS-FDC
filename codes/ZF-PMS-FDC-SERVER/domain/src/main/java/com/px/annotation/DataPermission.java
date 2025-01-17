package com.px.annotation;


import java.lang.annotation.*;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/2/13
 * created with IntelliJ IDEA.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 岗位数据权限 是否包含下级岗位
     * @return
     */
    boolean postChild() default true;

    /**
     * 部门数据权限
     * @return
     */
    boolean department() default false;

    /**
     * 忽略数据权限的部门
     * @return
     */
    String[] ignoreDepartment() default {};

    /**
     * 忽略数据权限的岗位
     * @return
     */
    String[] ignorePost() default {};

    /**
     * 团队数据 主要针对主管级别
     * @return
     */
    boolean teamData() default false;
}
