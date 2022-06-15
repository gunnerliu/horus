package cn.archliu.horus.server.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.common.exception.BaseException;
import cn.archliu.horus.common.exception.sub.ParamErrorException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-04-23 22:13:47
 * @Description: 基础异常捕获
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice
public class BaseExceptionAdvice {

    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ComRes<ResData<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorResult = String.join(",", errors);
        log.warn("参数校验异常：{}", errorResult);
        return ComRes.failed(errorResult);
    }

    @Order(value = Ordered.HIGHEST_PRECEDENCE - 1)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ComRes<ResData<Void>> bindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = "";
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }
        log.warn("参数校验异常：{}", message);
        return ComRes.failed(message);
    }

    @Order(value = Ordered.HIGHEST_PRECEDENCE - 2)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ComRes<ResData<Void>> methodNotSupport(HttpRequestMethodNotSupportedException e) {
        log.warn("请求类型不支持: {}", e.getMessage());
        return ComRes.failed("请求类型不支持!");
    }

    @Order(value = Ordered.HIGHEST_PRECEDENCE - 3)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ComRes<ResData<Void>> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("请求参数缺失: {}", e.getMessage());
        return ComRes.failed("请求参数缺失!");
    }

    @Order(value = Ordered.HIGHEST_PRECEDENCE - 4)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ComRes<ResData<Void>> messageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求参数异常: {}", e.getMessage());
        return ComRes.failed("请求参数异常!");
    }

    @Order(value = Ordered.LOWEST_PRECEDENCE + 2)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParamErrorException.class)
    public ComRes<ResData<Void>> catchParamException(ParamErrorException exception) {
        log.error("catch ParamErrorException: {}", exception.getMessage());
        return ComRes.failed(exception.getMessage());
    }

    @Order(value = Ordered.LOWEST_PRECEDENCE + 1)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BaseException.class)
    public ComRes<ResData<Void>> catchBaseException(BaseException exception) {
        log.error("catch unhandle base exception!", exception);
        return ComRes.failed("unhandle base exception !");
    }

}
