package com.iso.scale.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    private final AsyncProperties asyncProperties;

    public AsyncConfig(final AsyncProperties asyncProperties) {
        this.asyncProperties = asyncProperties;
    }

    @Lazy
    @Bean
    public ThreadPoolTaskExecutor customThreadPoolExecutor() {
        final ThreadPoolTaskExecutor customExecutor = new ThreadPoolTaskExecutor();

        customExecutor.setCorePoolSize(asyncProperties.getCorePoolSize());
        customExecutor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        customExecutor.setMaxPoolSize(asyncProperties.getQueueSize());

        return customExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return customThreadPoolExecutor();
    }
}
