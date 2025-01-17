package com.px.report.calculation.cityPrice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/9/14
 * created with IntelliJ IDEA.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableHead {

    String name() ;

    boolean isRequest() default false;

}
