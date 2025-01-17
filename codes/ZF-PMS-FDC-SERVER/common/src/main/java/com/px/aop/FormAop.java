package com.px.aop;

import com.google.common.cache.Cache;
import com.px.annotation.FormValid;
import com.px.exception.BusinessException;
import com.px.exception.ErrorCode;
import com.px.utils.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.px.common.Constants.FORM_TOKEN_IN_VALID;
import static com.px.common.Constants.FORM_TOKEN_VALID;

/**
 * @Auther: wucl
 * @Date: 2019/5/16
 */
@Component
@Aspect
@Slf4j
public class FormAop {

    @Autowired
    @Qualifier("formTokenCache")
    private Cache<String,String> formTokenCache;
    /**
     * 表单校验
     */
    @Pointcut("@annotation(com.px.annotation.FormValid)")
    public void formPoint() {
    }

    @Around("formPoint()")
    public synchronized Object formValidate(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        String key = null;
        try {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            FormValid formValid = method.getAnnotation(FormValid.class);
            if (formValid != null) {
                String formToken = HttpKit.getRequestFormValue("formToken");
                if (StringUtils.isBlank(formToken)) {
                    ErrorCode.throwBusinessException(ErrorCode.FORM_EXPIRED);
                }

                key = formToken;
                //防止并发情况下同时取到一样的token,即使用后马上设为无效token，并发情况下只有一个线程能取到有效token
                String value = formTokenCache.getIfPresent(key);
                //如果缓存找不到token，则说明token已过期
                if (StringUtils.isBlank(value)) {
                    ErrorCode.throwBusinessException(ErrorCode.FORM_EXPIRED);
                }else {
                    //取到之后将值设置为无效
                    formTokenCache.put(key,FORM_TOKEN_IN_VALID);
                    //检查缓存的value是否已无效，防止并发重复提交
                    if(!value.equals(FORM_TOKEN_VALID)){
                        ErrorCode.throwBusinessException(ErrorCode.FORM_DUPLICATE);
                    }

                }
            }
            result = pjp.proceed();
        } catch (Throwable e) {
            if (e instanceof BusinessException) {
                throw e;
            }
            log.error("表单验证异常", e);
        } finally {
        }
        return result;
    }
}
