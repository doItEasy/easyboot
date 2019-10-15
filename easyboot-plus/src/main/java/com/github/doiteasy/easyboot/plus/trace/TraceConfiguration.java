package com.github.doiteasy.easyboot.plus.trace;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

/**
 * 日志追踪配置
 *
 * @author feixiangming
 */
@Configuration
public class TraceConfiguration {

    @Bean
    public DefaultPointcutAdvisor traceAdvisor(Environment environment) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor() {
            @Override
            public Pointcut getPointcut() {
                return new StaticMethodMatcherPointcut() {
                    @Override
                    public boolean matches(Method method, Class<?> clazz) {
                        return method.getAnnotation(Trace.class) != null || clazz.getAnnotation(Trace.class) != null;
                    }
                };
            }
        };
        advisor.setAdvice(new TraceInterceptor(environment.getProperty("spring.application.name")));
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE + 1);
        return advisor;
    }
}
