package com.github.doiteasy.easyboot.plus.swagger;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger", ignoreUnknownFields = true)
@Data
public class SwaggerProperties {

    private String title = "Application API";

    private String description = "API documentation";

    private String version = "0.0.1";

    private String termsOfServiceUrl = null;

    private String contactName = null;

    private String contactUrl = null;

    private String contactEmail = null;

    private String license = null;

    private String licenseUrl = null;

    private String defaultIncludePattern = "/api/.*";

    private String host = null;

    private String[] protocols = {};

    private boolean useDefaultResponseMessages = true;


}
