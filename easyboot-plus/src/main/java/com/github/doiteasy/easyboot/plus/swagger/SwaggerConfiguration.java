package com.github.doiteasy.easyboot.plus.swagger;

/*
 * Copyright 2016-2018 the original author or authors from the Yinghuo project.
 *
 * This file is part of the Yinghuo project, see https://www.firefly.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Servlet;
import java.nio.ByteBuffer;
import java.util.*;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Springfox Swagger configuration.
 * <p>
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue.
 * In that case, you can use the "no-swagger" Spring profile, so that this bean is ignored.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({
        ApiInfo.class,
        BeanValidatorPluginsConfiguration.class,
        Servlet.class,
        DispatcherServlet.class
})
@Profile("swagger")
@AutoConfigureAfter(SwaggerProperties.class)
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    public static final String STARTING_MESSAGE = "Starting Swagger";
    public static final String STARTED_MESSAGE = "Started Swagger in {} ms";
    public static final String MANAGEMENT_TITLE_SUFFIX = "Management API";
    public static final String MANAGEMENT_GROUP_NAME = "management";
    public static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    private final SwaggerProperties properties;

    public SwaggerConfiguration(SwaggerProperties properties) {
        this.properties = properties;
    }

    /**
     * Springfox configuration for the API Swagger docs.
     *
     * @param swaggerCustomizers Swagger customizers
     * @param alternateTypeRules alternate type rules
     * @return the Swagger Springfox configuration
     */
    @Bean
    @ConditionalOnMissingBean(name = "swaggerSpringfoxApiDocket")
    public Docket swaggerSpringfoxApiDocket(List<SwaggerCustomizer> swaggerCustomizers,
                                            ObjectProvider<AlternateTypeRule[]> alternateTypeRules) {
        log.debug(STARTING_MESSAGE);
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = createDocket();

        // Apply all SwaggerCustomizers orderly.
        swaggerCustomizers.forEach(customizer -> customizer.customize(docket));

        // Add all AlternateTypeRules if available in spring bean factory.
        // Also you can add your rules in a customizer bean above.
        Optional.ofNullable(alternateTypeRules.getIfAvailable()).ifPresent(docket::alternateTypeRules);

        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        return docket;
    }

    @Bean
    public MySwaggerCustomizer fireflySwaggerCustomizer() {
        return new MySwaggerCustomizer(properties);
    }


    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.autoconfigure.ManagementServerProperties")
    @ConditionalOnProperty("management.endpoints.web.base-path")
    @ConditionalOnExpression("'${management.endpoints.web.base-path}'.length() > 0")
    @ConditionalOnMissingBean(name = "swaggerSpringfoxManagementDocket")
    public Docket swaggerSpringfoxManagementDocket(@Value("${spring.application.name:application}") String appName,
                                                   @Value("${management.endpoints.web.base-path}") String managementContextPath) {

        ApiInfo apiInfo = new ApiInfo(
                StringUtils.capitalize(appName) + " " + MANAGEMENT_TITLE_SUFFIX,
                MANAGEMENT_DESCRIPTION,
                properties.getVersion(),
                "",
                ApiInfo.DEFAULT_CONTACT,
                "",
                "",
                new ArrayList<>()
        );

        return createDocket()
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
                .groupName(MANAGEMENT_GROUP_NAME)
                .host(properties.getHost())
                .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(managementContextPath + ".*"))
                .build();
    }

    protected Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2);
    }



}

