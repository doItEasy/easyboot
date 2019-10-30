package com.github.doiteasy.easyboot.common.enums;


/**
 *
 */
public enum DeletedStatusEnum {

    DELETED_NO(0, "正常(未删除)"),
    DELETED_YES(1, "删除");

    /**
     * 状态值
     */
    private Integer value;
    /**
     * 状态名
     */
    private String name;

    DeletedStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
