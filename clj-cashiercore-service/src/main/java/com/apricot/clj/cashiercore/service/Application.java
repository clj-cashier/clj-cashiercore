package com.apricot.clj.cashiercore.service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.apricot.framework.extend.apollo.listener.LoggerLevelConfigChangeListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author auto
 */
@SpringBootApplication
@EnableAsync
@ComponentScan("com.apricot.clj.cashiercore.*")
@DubboComponentScan(basePackages = {"com.apricot.framework.extend.dubbo.filter", "com.apricot.clj.cashiercore.service.facade.impl"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 集成动态日志等级
     *
     * @return
     */
    @Bean
    public LoggerLevelConfigChangeListener loggerLevelConfigChangeListener() {
        return new LoggerLevelConfigChangeListener();
    }
}
