package com.ss.test.simple.ms.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author JavaSaBr
 */
@SpringBootApplication
public class SimpleMsCacheApplication {

    @Value("${application.key.value.service.clean.interval}")
    private long cleanInterval;

    @Value("${application.key.value.service.life.time}")
    private long lifeTime;

    @Value("${application.key.value.service.hot.cache.max.size}")
    private int hotCacheMaxSize;

    public static void main(final String[] args) {
        SpringApplication.run(SimpleMsCacheApplication.class, args);
    }

    public long getCleanInterval() {
        return cleanInterval;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public int getHotCacheMaxSize() {
        return hotCacheMaxSize;
    }
}
