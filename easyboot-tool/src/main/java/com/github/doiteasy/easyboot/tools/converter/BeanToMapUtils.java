package com.github.doiteasy.easyboot.tools.converter;


import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class BeanToMapUtils {

    public static Map<String, Object> toMap(Object object) {
        boolean isBeanToMapObject = object.getClass().isAnnotationPresent(BeanToMapObject.class);
        if (isBeanToMapObject) {
            return toMapWithFiledName(object);
        }
        return BeanMap.create(object);
    }

    private static Map<String, Object> toMapWithFiledName(Object object) {
        Map<String, Object> maps = Maps.newLinkedHashMap();
        Arrays.stream(object.getClass().getDeclaredFields())
            .filter(t -> t.isAnnotationPresent(BeanToMapField.class))
            .sorted(Comparator.comparing(t -> t.getAnnotation(BeanToMapField.class).order()))
            .forEach(t -> {
                t.setAccessible(true);
                String displayName = t.getAnnotation(BeanToMapField.class).displayName();
                Object value;
                try {
                    value = t.get(object);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                maps.put(displayName, value);
            });
        return maps;
    }

}
