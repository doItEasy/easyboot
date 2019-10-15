package com.github.doiteasy.easyboot.plus.trace;

import com.alibaba.fastjson.JSONObject;
import com.github.doiteasy.easyboot.tools.utils.StrUtil;
import com.github.doiteasy.easyboot.tools.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志摘要
 */
@Slf4j
public class TraceInterceptor implements MethodInterceptor {
    private static final org.slf4j.Logger traceLog = LoggerFactory.getLogger("TRACE");

    public static final String CONSTANT_UID = "TRACE_USER_ID";

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String TRACE_ID_NAME = "X-B3-TraceId";
    private static final String SPAN_ID_NAME = "X-B3-SpanId";

    private static final String LOG_COMMON_TYPE = "type";
    private static final String LOG_COMMON_TIME = "time";
    private static final String LOG_COMMON_TRACE_ID = "traceId";
    private static final String LOG_COMMON_SPAN_ID = "spanId";
    private static final String LOG_COMMON_APP = "app";
    private static final String LOG_COMMON_DESC = "desc";
    private static final String LOG_COMMON_METHOD = "method";
    private static final String LOG_COMMON_TAKE = "take";
    private static final String LOG_COMMON_ERROR = "error";
    private static final String LOG_COMMON_ERROR_STACK = "errorStack";
    private static final String LOG_STATE = "auditstrategy";
    private static final String LOG_PARAM = "param";
    private static final String LOG_RET = "ret";
    private static final String LOG_UID = "uid";
    private static final String LOG_URI = "uri";
    private static final String LOG_QUERY = "query";
    private static final String LOG_REMOTE = "remote";
    private static final String LOG_UA = "ua";

    private String applicationName;

    public TraceInterceptor(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        TraceIngore traceIngore = method.getDeclaringClass().getAnnotation(TraceIngore.class);
        if (traceIngore != null) {
            return invocation.proceed();
        }
        traceIngore = method.getAnnotation(TraceIngore.class);
        if (traceIngore != null) {
            return invocation.proceed();
        }

        long startTime = System.currentTimeMillis();
        Object proceedValue = null;
        Throwable throwable = null;
        Map<String, Object> before = null;
        Map<String, Object> after = null;
        try {
            try {
                before = before(invocation, startTime);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
            return proceedValue = invocation.proceed();
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            try {
                after = after(invocation, startTime, proceedValue, throwable);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
            if (before == null) {
                before = new LinkedHashMap();
            }
            if (after == null) {
                after = new LinkedHashMap();
            }
            before.putAll(after);
            traceLog.info(JSONObject.toJSONString(before));
        }
    }

    private Map<String, Object> before(MethodInvocation invocation, long startTime) {
        Method method = invocation.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        Trace clazzTrace = method.getDeclaringClass().getAnnotation(Trace.class);
        Trace methodTrace = method.getAnnotation(Trace.class);
        TraceInfo traceInfo = mergeClassAndMethod(clazzTrace, methodTrace);
        String traceId = MDC.get(TRACE_ID_NAME);
        String spanId = MDC.get(SPAN_ID_NAME);

        Map<String, Object> info = new LinkedHashMap();
        info.put(LOG_COMMON_TIME, DateFormatUtils.format(startTime, TIME_PATTERN));
        info.put(LOG_COMMON_TRACE_ID, traceId);
        info.put(LOG_COMMON_SPAN_ID, spanId);
        info.put(LOG_COMMON_TYPE, "BEGIN");
        info.put(LOG_COMMON_METHOD, methodName);
        info.put(LOG_COMMON_APP, applicationName);
        info.put(LOG_COMMON_DESC, traceInfo.getDesc());
        if (traceInfo.isTraceParam()) {
            info.put(LOG_PARAM, StrUtil.shortObj(invocation.getArguments(), 36));
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (httpServletRequest != null) {
                info.put(LOG_UID, httpServletRequest.getAttribute(CONSTANT_UID));
                info.put(LOG_URI, httpServletRequest.getRequestURI());
                info.put(LOG_QUERY, httpServletRequest.getQueryString());
                info.put(LOG_REMOTE, WebUtil.getIpAddress(httpServletRequest));
                info.put(LOG_UA, httpServletRequest.getHeader(HttpHeaders.USER_AGENT));
            }
        }

        clearEmpty(info);
        return info;
    }

    private Map<String, Object> after(MethodInvocation invocation, long startTime, Object proceedValue, Throwable throwable) {
        long nowMillis = System.currentTimeMillis();
        long elapseTime = nowMillis - startTime;

        Method method = invocation.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        Trace clazzTrace = method.getDeclaringClass().getAnnotation(Trace.class);
        Trace methodTrace = method.getAnnotation(Trace.class);
        TraceInfo traceInfo = mergeClassAndMethod(clazzTrace, methodTrace);
        String traceId = MDC.get(TRACE_ID_NAME);
        String spanId = MDC.get(SPAN_ID_NAME);
        int state = 1;

        Map<String, Object> info = new LinkedHashMap();
        info.put(LOG_COMMON_TIME, DateFormatUtils.format(startTime, TIME_PATTERN));
        info.put(LOG_COMMON_TRACE_ID, traceId);
        info.put(LOG_COMMON_SPAN_ID, spanId);
        info.put(LOG_COMMON_TYPE, "AFTER");
        info.put(LOG_COMMON_METHOD, methodName);
        if (throwable != null) {
            state = 0;
        }
        info.put(LOG_STATE, state);
        info.put(LOG_COMMON_TAKE, elapseTime);
        if (proceedValue != null && traceInfo.isTraceRet()) {
            info.put(LOG_RET, StrUtil.shortObj(proceedValue, 10));
        }
        if (throwable != null) {
            info.put(LOG_COMMON_ERROR_STACK, StringUtils.substring(ExceptionUtils.getStackTrace(throwable), 0, 300));
            info.put(LOG_COMMON_ERROR, throwable.getClass().getSimpleName() + ":" + StringUtils.trimToEmpty(throwable.getMessage()));
        }

        clearEmpty(info);
        return info;
    }

    private void clearEmpty(Map<String, Object> info) {
        Iterator<Map.Entry<String, Object>> iterator = info.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            if (value == null) {
                iterator.remove();
            } else if (value instanceof String) {
                if (((String) value).trim().length() == 0 || ((String) value).trim().equalsIgnoreCase("null")) {
                    iterator.remove();
                }
            }
        }
    }

    private TraceInfo mergeClassAndMethod(Trace clazzTrace, Trace methodTrace) {
        TraceInfo traceInfo = new TraceInfo();
        traceInfo.setTraceParam(true);
        traceInfo.setTraceRet(true);
        setInfo(clazzTrace, traceInfo);
        setInfo(methodTrace, traceInfo);
        return traceInfo;
    }

    private void setInfo(Trace trace, TraceInfo traceInfo) {
        if (trace != null) {
            if (StringUtils.isNotBlank(trace.desc())) {
                if (StringUtils.isNotBlank(traceInfo.getDesc())) {
                    traceInfo.setDesc(traceInfo.getDesc() + "-" + trace.desc());
                } else {
                    traceInfo.setDesc(trace.desc());
                }
            }
            if (!trace.traceParam()) {
                traceInfo.setTraceParam(false);
            }
            if (!trace.traceRet()) {
                traceInfo.setTraceRet(false);
            }
        }
    }

}
