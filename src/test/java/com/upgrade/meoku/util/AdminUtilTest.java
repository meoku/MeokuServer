package com.upgrade.meoku.util;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
public class AdminUtilTest {

    @Test
    @DisplayName("OCR로 가져온 메뉴데이터 특수문자 및 공백 제거 Util method Test")
    @Disabled // 테스트 완료
    public void testOfRemoveCharacters(){
        String str = "new^테스트*테스트 / 테스트 * ⇡";
        System.out.println(MeokuUtil.removeCharacters(str));
    }

    @Test
    @DisplayName("토큰 발급에 필요한 시크릿 키 발급")
    public void createScretKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 32바이트 키
        random.nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println(secretKey);
    }
}
