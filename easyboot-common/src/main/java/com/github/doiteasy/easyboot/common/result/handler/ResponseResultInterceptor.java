package com.github.doiteasy.easyboot.common.result.handler;

import com.github.doiteasy.easyboot.common.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@Slf4j
public class ResponseResultInterceptor implements HandlerInterceptor {

    private static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("==========================================1================================================");

        if(handler instanceof HandlerMethod){
            //获取此请求，是否需要返回值包装，设置一个属性标记。
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if( clazz.isAnnotationPresent(ResponseResult.class) ) {
                //设置此请求返回体需要包装，往下传递，在responseAdvice层处理
                request.setAttribute(RESPONSE_RESULT_ANN,clazz.getAnnotation(ResponseResult.class));
            }else if(method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(RESPONSE_RESULT_ANN,method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
