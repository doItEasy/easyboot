package com.github.doiteasy.easyboot.tools.excel;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author feixm
 */
@Target({ElementType.FIELD,ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ExcelResources{
    /**
     * 属性的标题名称
     * @return
     */
    String title() default "";
    /**
     * excel列的序号，导入取excel的index列
     * @return
     */
    int index() default 9999;

    /**
     * 导入的开始行
     * @return
     */
    int width() default 200;

    /**
     * 是否使用下拉框
     * @return
     */
    boolean enableSelect() default false;
}
