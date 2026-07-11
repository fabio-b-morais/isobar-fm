package com.dws.isobarfm.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/** Builds the {@link RestTemplate} used to call the upstream bands-api. */
@Configuration
@EnableConfigurationProperties(BandsApiProperties.class)
public class RestClientConfig {

    @Bean
    public RestTemplate bandsApiRestTemplate(RestTemplateBuilder builder,
                                             BandsApiProperties properties) {
        return builder
                .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeoutMs()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeoutMs()))
                .build();
    }
}
