package com.upgrade.meoku.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:/naver-cloud-config.yml")
public class NaverCloudConfig {
    @Value("${naver_ocr_url}")
    private String naverOcrUrl;
    @Value("${naver_api_scret_key}")
    private String naverApiScretKey;
    @Value("${TEMPLATEIDS}")
    private int TEMPLATEIDS;
}
