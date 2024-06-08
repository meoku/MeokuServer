package com.upgrade.meoku.util;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminUtilTest {

    @Test
    @DisplayName("OCR로 가져온 메뉴데이터 특수문자 및 공백 제거 Util method Test")
    @Disabled // 테스트 완료
    public void testOfRemoveCharacters(){
        String str = "new^테스트*테스트 / 테스트 * ⇡";
        System.out.println(MeokuUtil.removeCharacters(str));
    }
}
