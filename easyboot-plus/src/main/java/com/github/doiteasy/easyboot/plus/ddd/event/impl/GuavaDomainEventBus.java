/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.doiteasy.easyboot.plus.ddd.event.impl;

import com.github.doiteasy.easyboot.plus.ddd.event.DomainEvent;
import com.github.doiteasy.easyboot.plus.ddd.event.DomainEventBus;
import com.github.doiteasy.easyboot.plus.ddd.event.handler.DomainEventHandler;
import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class GuavaDomainEventBus implements DomainEventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaDomainEventBus.class);
    private final AsyncEventBus asyncEventBus;

    public GuavaDomainEventBus(Executor asyncExecutor) {
        this.asyncEventBus = new AsyncEventBus(asyncExecutor);
    }

    public AsyncEventBus getAsyncEventBus() {
        return asyncEventBus;
    }

    @Override
    public void dispatch(DomainEvent domainEvent) {
        asyncEventBus.post(domainEvent);
    }

    @Override
    public void register(DomainEventHandler domainEventHandler) {
        asyncEventBus.register(domainEventHandler);
    }
}
