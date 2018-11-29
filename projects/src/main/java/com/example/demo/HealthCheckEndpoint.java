package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author kejun
 * @date 2018/11/29 下午4:34
 */
@ConfigurationProperties(prefix = "endpoints.healthcheck")
@Component
public class HealthCheckEndpoint extends AbstractEndpoint<String> implements ApplicationRunner {
    private static final Logger log  = LoggerFactory.getLogger(HealthCheckEndpoint.class);
    private volatile String status = "notready";

    public HealthCheckEndpoint()

    { super("healthcheck"); }

    @Override
    public String invoke()

    { return this.status; }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.status = "success";
        log.info("application status is changed to success!");
    }

}
