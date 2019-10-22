package com.github.doiteasy.easyboot.common.log;

import com.github.doiteasy.easyboot.common.exceptions.EasyBootException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.Arrays;


@Aspect
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Environment env;

    public LoggingAspect(Environment env) {
        this.env = env;
    }


    @Pointcut( "@annotation(com.github.doiteasy.easyboot.common.log.Log)" +
            " || within(@org.springframework.stereotype.Service *)" +
        " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void logPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }




    @Around("logPointcut()")
    public Object logHandler(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime=System.currentTimeMillis();
        String methodName= joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String params =  Arrays.toString(joinPoint.getArgs());
        Object result= null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            String exception=throwable.getClass()+":"+throwable.getMessage();
            long costTime=System.currentTimeMillis()-startTime;
            if(throwable instanceof  EasyBootException){
                log.warn("请求类名：{}，请求方法：{}，请求参数:{}，请求结果：{}，请求耗时：{}，",className,methodName,params,exception,costTime);
            }else{
                log.error("请求类名：{}，请求方法：{}，请求参数:{}，请求结果：{}，请求耗时：{}，",className,methodName,params,exception,costTime);
            }
            throw  throwable;
        }
        long costTime=System.currentTimeMillis()-startTime;
        log.info("请求类名：{}，请求方法：{}，请求参数:{}，请求结果：{}，请求耗时：{}",className,methodName,params,result,costTime);
        return result;
    }


//    @AfterThrowing(pointcut = " logPointcut()", throwing = "e")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
//        if(e instanceof EasyBootException) {
//            log.warn("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
//        } else {
//            log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
//        }
//    }



}
