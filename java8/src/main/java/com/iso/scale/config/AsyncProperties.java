package com.iso.scale.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("async")
public class AsyncProperties {

    private static final int DEFAULT_CORE_POOL_SIZE = 19;

    private static final int DEFAULT_MAX_POOL_SIZE = 5000;

    private static final int DEFAULT_QUEUE_SIZE = 5000;

    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;

    private int queueSize = DEFAULT_QUEUE_SIZE;
}
