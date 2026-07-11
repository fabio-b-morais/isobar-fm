package com.dws.isobarfm.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Externalised configuration for the upstream bands-api (see application.yml). */
@ConfigurationProperties(prefix = "bands-api")
public class BandsApiProperties {

    private String baseUrl;
    private int connectTimeoutMs = 3000;
    private int readTimeoutMs = 5000;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }
}
