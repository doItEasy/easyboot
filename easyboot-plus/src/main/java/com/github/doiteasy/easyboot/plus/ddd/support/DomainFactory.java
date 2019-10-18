package com.github.doiteasy.easyboot.plus.ddd.support;


import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import javax.annotation.Resource;

public abstract class DomainFactory<T extends Entity> {

    @Resource
    protected AutowireCapableBeanFactory spring;
    

    public DomainFactory<T> setSpring(AutowireCapableBeanFactory spring) {
        this.spring = spring;
        return this;
    }
}
