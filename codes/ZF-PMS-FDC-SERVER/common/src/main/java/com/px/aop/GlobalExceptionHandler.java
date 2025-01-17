package com.px.aop;

import com.px.exception.BusinessException;
import com.px.exception.file.ExcelParseException;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import com.px.common.RestResponse;
import com.px.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * 异常处理器
 *
 * @author 品讯科技
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(DuplicateKeyException.class)
    public RestResponse handleDuplicateKeyException(DuplicateKeyException e) {
        return RestResponse.error(ErrorCode.DATA_EXIST.getErrorMsg(), Integer.valueOf(ErrorCode.DATA_EXIST.getCode()));
    }

    @ExceptionHandler({ParamsInValidException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public RestResponse handleParamsValidException(Exception e) {
        if (e instanceof ParamsInValidException) {
            return RestResponse.error(e.getMessage(), Integer.valueOf(ErrorCode.PARAM_ERROR.getCode()));
        } else {
            //jsr303
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder errMsg = new StringBuilder();
            StringBuilder errMsg2Show = new StringBuilder();
            for (ObjectError allError : allErrors) {
                FieldError fieldError = (FieldError) allError;
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                errMsg.append(field + ":" + defaultMessage + "; ");
                errMsg2Show.append(" " + defaultMessage + "; ");
            }
            logger.error("handleParamsValidException errMsg is  {}", errMsg.toString());
            return RestResponse.error(errMsg2Show.toString(), Integer.valueOf(ErrorCode.PARAM_ERROR.getCode()));
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResponse handlerNoFoundException(Exception e) {
        return RestResponse.error("404 资源不存在，请检查路径是否正确", HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(ExcelParseException.class)
    public RestResponse handlerExcelImportException(ExcelParseException e) {
        logger.error("excel解析出错  e {}", e.getMessage(), e);
        return RestResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BusinessException.class)
    public RestResponse handleBusinessException(BusinessException e) {
        return RestResponse.error(e.getMessage(), e.getCode());
    }


    @ExceptionHandler(IllegalStateException.class)
    public RestResponse handleBusinessException(IllegalStateException e) {
        logger.error(e.getMessage(), e);
        if (e.getMessage().contains("Duplicate key")) {
            return RestResponse.error("基础数据重复 " + e.getMessage());
        }
        return RestResponse.error("系统出错了");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public RestResponse handleException(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return RestResponse.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public RestResponse runtimeError(RuntimeException e) {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        logger.error("系统出错了:", e);
        if (e.getMessage().contains("Data too long")) {
            return RestResponse.error("存在数据过长字段，请检查。");
        }
        if ( e instanceof HttpMessageNotReadableException){
            return RestResponse.error("参数格式不正确，请配置正确参数类型。");
        }
        return RestResponse.error("系统出错了");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(BindException.class)
    public RestResponse bindError(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder errMsg = new StringBuilder();
        for (ObjectError allError : allErrors) {
            FieldError fieldError = (FieldError) allError;
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            errMsg.append(field + ":" + defaultMessage + ";  ");
        }
        logger.error("参数错误,原因:" + errMsg.toString());
        return RestResponse.error(errMsg.toString(), Integer.valueOf(ErrorCode.PARAM_ERROR.getCode()));
    }

}