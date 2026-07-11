package com.dws.isobarfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Application entry point.
 *
 * <p>{@code @EnableCaching} turns on the Spring Cache abstraction; the actual
 * provider (Caffeine) is wired in {@code infrastructure.config.CacheConfig}.
 */
@EnableCaching
@SpringBootApplication
public class IsobarFmApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsobarFmApplication.class, args);
    }
}
