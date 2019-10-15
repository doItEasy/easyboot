package com.github.doiteasy.easyboot.tools.copy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by feixm
 */
@Component
@Slf4j
public class CopyValueUtil {


    public <T> T copyFromObj(Object from, T to) {

        Class<?> fromclazz = from.getClass();
        Class<?> toclazz = to.getClass();

        Field[] fromFields = fromclazz.getDeclaredFields();
        for (Field field : fromFields) {

            String toKey = field.getName();

            if (field.isAnnotationPresent(CopyMapping.class)) {
                CopyMapping copyMapping = field.getAnnotation(CopyMapping.class);
                if (!StringUtils.isEmpty(copyMapping.alias())) {
                    toKey = copyMapping.alias();
                }
            }
            try {
                Field toField = toclazz.getDeclaredField(toKey);

                field.setAccessible(true);

                Object fieldValue = field.get(from);

                if (fieldValue == null) {
                    continue;
                }

                toField.setAccessible(true);

                toField.set(to, fieldValue);

            } catch (NoSuchFieldException e) {
//                log.info("fromclazz has no filed:{}", toKey);
//                log.info(e.getMessage(), e);
                continue;
            } catch (IllegalAccessException e) {
//                log.info("from Field read value failed:{}", field.getName());
//                log.info(e.getMessage(), e);
            } catch (IllegalArgumentException e) {
//                log.info("from Field read value failed:{}", field.getName());
//                log.info(e.getMessage(), e);
            }


        }

        return to;
    }

    public <T> T copyFromMap(Map<String, Object> from, T to) {

        Class<?> toclazz = to.getClass();

        from.forEach((k, v) -> {
            try {
                Field toField = toclazz.getDeclaredField(k);
                toField.set(to, v);
            } catch (NoSuchFieldException e) {
                log.info("toclazz has no filed:{}", k);
                log.info(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.info(e.getMessage(), e);
            }
        });

        return to;
    }

}
