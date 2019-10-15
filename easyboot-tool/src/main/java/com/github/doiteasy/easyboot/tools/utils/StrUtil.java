package com.github.doiteasy.easyboot.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class StrUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuid(String preFix) {
        return preFix + UUID.randomUUID().toString().replace("-", "").substring(preFix.length());
    }

    public static String shortStr(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (StringUtils.length(str) > maxLength) {
            str = StringUtils.substring(str, 0, maxLength - 1) + "~";
        }
        return str;
    }

    private static List<Class> jsonIgnoreClass = new ArrayList<>();

    static {
        jsonIgnoreClass.add(ServletRequest.class);
        jsonIgnoreClass.add(ServletResponse.class);
        jsonIgnoreClass.add(HttpSession.class);
    }

    private static PropertyPreFilter preFilter = (jsonSerializer, o, s) -> {
        if (o == null) {
            return false;
        }
        for (Class ignoreClass : jsonIgnoreClass) {
            if (ignoreClass.isAssignableFrom(o.getClass())) {
                return false;
            }
        }
        return true;
    };

    /**
     * 缩短
     *
     * @param obj
     * @param maxLength
     * @return
     */
    public static String shortObj(Object obj, int maxLength) {
        if (obj == null) {
            return null;
        }
        try {
            String text = JSONObject.toJSONString(obj, preFilter, SerializerFeature.IgnoreNonFieldGetter);
            if (text != null) {
                if (text.startsWith("[")) {
                    JSONArray ja = JSONObject.parseArray(text);
                    return JSONObject.toJSONString(parse(ja, new ShortParser(maxLength)), JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.UseSingleQuotes, SerializerFeature.IgnoreNonFieldGetter);
                } else if (text.startsWith("{")) {
                    JSONObject jo = JSONObject.parseObject(text);
                    return JSONObject.toJSONString(parse(jo, new ShortParser(maxLength)), JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.UseSingleQuotes, SerializerFeature.IgnoreNonFieldGetter);
                } else {
                    return shortStr(obj.toString(), maxLength);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public interface Parser {
        boolean accept(String key, Object value);

        Object parse(String key, Object value);
    }

    /**
     * 缩短对象字段值
     */
    static class ShortParser implements Parser {
        private Integer maxLength;

        ShortParser(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public boolean accept(String key, Object value) {
            return value instanceof String;
        }

        @Override
        public Object parse(String key, Object value) {
            String str = (String) value;
            return shortStr(str, maxLength);
        }
    }

    /**
     * 脱敏对象字段值
     */
    static class StarParser implements Parser {
        private Integer startLength;

        StarParser(int startLength) {
            this.startLength = startLength;
        }

        @Override
        public boolean accept(String key, Object value) {
            return value instanceof String;
        }

        @Override
        public Object parse(String key, Object value) {
            throw new RuntimeException("not finish");
        }
    }

    public static JSONArray parse(JSONArray obj, Parser parser) {
        JSONArray newObj = new JSONArray();
        for (int i = 0; i < obj.size(); i++) {
            Object value = obj.get(i);
            if (parser.accept(null, value)) {
                value = parser.parse(null, value);
                newObj.add(value);
                continue;
            }
            if (value instanceof JSONObject) {
                newObj.add((parse((JSONObject) value, parser)));
            } else if (value instanceof JSONArray) {
                newObj.add((parse((JSONArray) value, parser)));
            } else {
                newObj.add(value);
            }
        }
        return newObj;
    }

    public static JSONObject parse(JSONObject obj, Parser parser) {
        JSONObject newObj = new JSONObject();
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            Object value = entry.getValue();
            if (parser.accept(entry.getKey(), value)) {
                value = parser.parse(entry.getKey(), value);
                newObj.put(entry.getKey(), value);
                continue;
            }
            if (value instanceof JSONObject) {
                newObj.put(entry.getKey(), (parse((JSONObject) value, parser)));
            } else if (value instanceof JSONArray) {
                newObj.put(entry.getKey(), (parse((JSONArray) value, parser)));
            } else {
                newObj.put(entry.getKey(), value);
            }
        }
        return newObj;
    }
}
