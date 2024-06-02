package com.upgrade.meoku.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/api-config.yml")
public class RequestApiConfig {
    @Value("${weatherApiEncodingKey}")
    private String weatherApiEncodingKey;

    @Value("${weatherApiDecodingKey}")
    private String weatherApiDecodingKey;

    public String getWeatherApiEncodingKey() {
        return weatherApiEncodingKey;
    }

    public String getWeatherApiDecodingKey() {
        return weatherApiDecodingKey;
    }

}

