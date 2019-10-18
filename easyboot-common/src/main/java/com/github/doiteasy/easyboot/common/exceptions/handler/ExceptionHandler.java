package com.github.doiteasy.easyboot.common.exceptions.handler;



import com.github.doiteasy.easyboot.common.exceptions.EasyBootException;
import com.github.doiteasy.easyboot.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EasyBootException.class)
    @ResponseBody
    public Result handleException(HttpServletRequest request, EasyBootException ex) {
        log.error("exception error:{}",ex);

        Result message = new Result(ex.getErrorCode(),ex.getMessage());
        return message;
    }

}
