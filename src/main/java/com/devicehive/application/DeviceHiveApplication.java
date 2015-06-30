package com.devicehive.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
@ComponentScan("com.devicehive")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EntityScan(basePackages = {"com.devicehive.model"})
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class DeviceHiveApplication extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DeviceHiveApplication.class);

    public static final String WAIT_EXECUTOR = "DeviceHiveWaitService";
    public static final String MESSAGE_EXECUTOR = "DeviceHiveMessageService";

    public static void main(String ... args) {
        SpringApplication.run(DeviceHiveApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DeviceHiveApplication.class);
    }

    @Bean
    public Validator localValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Lazy(false)
    @Bean(name = WAIT_EXECUTOR)
    public ExecutorService waitExecutorService() {
        return Executors.newFixedThreadPool(32);
    }

    @Lazy(false)
    @Bean(name = MESSAGE_EXECUTOR)
    public ExecutorService messageExecutorService() {
        return Executors.newFixedThreadPool(32);
    }
}