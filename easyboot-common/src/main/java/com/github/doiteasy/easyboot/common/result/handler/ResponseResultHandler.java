package com.github.doiteasy.easyboot.common.result.handler;

import com.github.doiteasy.easyboot.common.result.ResponseResult;
import com.github.doiteasy.easyboot.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        ResponseResult result = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return result==null ? false : true;

    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if(body instanceof Result){
            return  body;
        }

        return Result.success(body);
    }
}
