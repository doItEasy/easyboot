package com.github.doiteasy.easyboot.plus.ddd.support;


import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import javax.annotation.Resource;
import java.util.Optional;

public abstract class DomainRepository<T extends Entity, R> {

    @Resource
    protected AutowireCapableBeanFactory spring;

    public abstract boolean exists(T t);

    public abstract void save(T t);

    protected abstract Optional<T> findOneByEntityNo(R entityNo);

    public T load(R entityNo) {
        return findOneByEntityNo(entityNo)
            .map(t -> {
                spring.autowireBean(t);
                return t;
            })
            .orElse(null);
    }

    public void setSpring(AutowireCapableBeanFactory spring) {
        this.spring = spring;
    }
}
