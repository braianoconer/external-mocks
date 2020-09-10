package com.co.mocks.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Getter
@Setter
@ConfigurationProperties("service")
public class ServiceConfig {

    private String env;
    private String chartVersion;

}
