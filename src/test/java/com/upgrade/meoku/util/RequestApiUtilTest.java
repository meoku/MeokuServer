package com.upgrade.meoku.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RequestApiUtilTest {

    @Test
    @DisplayName("현재 날짜, 시간 기상청 API 요청 호출에 맞게 변경된 형태로 출력")
    public void getCurDateAndTimetest() {
        String curDate = RequestApiUtil.getTodayDate();
        String curTime = RequestApiUtil.getCurrentTime();

        System.out.println(curDate);
        System.out.println(curTime);
    }

    @Test
    @DisplayName("단기 예보를 위해 지정된 시간 가져오는 함수 잘 작동하는지 테스트")
    public void getRequestTimeForShortTermForecastRequest(){
        String requestTime = RequestApiUtil.getRequestTimeForShortTermForecastRequest();
        System.out.println(requestTime);
    }
}
