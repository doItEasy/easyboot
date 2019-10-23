package com.github.doiteasy.easyboot.plus.idempotent.handle;

import com.github.doiteasy.easyboot.plus.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class IdempotentHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotentHandler.class);

    @Pointcut("@annotation(com.github.doiteasy.easyboot.plus.idempotent.annotation.Idempotent)")
    private void point() {
    }

    @Around("point()")
    public void around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Idempotent idempotent = signature.getMethod().getAnnotation(Idempotent.class);
        //to do
    }
}
