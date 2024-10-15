package com.upgrade.meoku.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


//@Getter 써도 생성 안돼서 직접 Getter 생성
@Component
public class NaverCloudConfig {
    @Value("${naver_ocr_url}")
    private String naverOcrUrl;
    @Value("${naver_api_secret_key}")
    private String naverApiScretKey;
    @Value("${TEMPLATEIDS}")
    private int TEMPLATEIDS;

    public String getNaverOcrUrl() {
        return naverOcrUrl;
    }

    public String getNaverApiScretKey() {
        return naverApiScretKey;
    }

    public int getTEMPLATEIDS() {
        return TEMPLATEIDS;
    }
}
