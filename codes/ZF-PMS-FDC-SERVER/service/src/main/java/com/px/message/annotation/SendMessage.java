package com.px.message.annotation;

import com.px.message.MessageTypeEnum;

import java.lang.annotation.*;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/11/1
 * created with IntelliJ IDEA.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SendMessage {

    MessageTypeEnum messageType();
}
