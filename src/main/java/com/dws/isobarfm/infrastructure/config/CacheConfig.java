package com.dws.isobarfm.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/** Caching layer: Caffeine (in-JVM) behind Spring's cache abstraction, with a configurable TTL. */
@Configuration
public class CacheConfig {

    public static final String BANDS_CACHE = "bands";
    public static final String BAND_CACHE = "band";
    public static final String ALBUM_CACHE = "album";

    @Bean
    public CacheManager cacheManager(@Value("${cache.ttl-minutes}") long ttlMinutes,
                                     @Value("${cache.maximum-size}") long maximumSize) {
        CaffeineCacheManager cacheManager =
                new CaffeineCacheManager(BANDS_CACHE, BAND_CACHE, ALBUM_CACHE);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ttlMinutes, TimeUnit.MINUTES)
                .maximumSize(maximumSize)
                .recordStats());
        return cacheManager;
    }
}
