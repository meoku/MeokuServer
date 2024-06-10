package com.upgrade.meoku.weather.api;

import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KMAApiServiceTest {

    @Test
    @DisplayName("위임 구조로 만든 초단기 실황 API 호출 테스트")
    public void KMAApiUltraShortTermTest() throws Exception {

        KMAApiService kmaApiService = new KMAAPIUltraShortTerm();

//        WeatherDataDTO requestedWeatherDTO = kmaApiService.requestWeatherApi();
//        System.out.println(requestedWeatherDTO.toString());

    }

    @Test
    @DisplayName("위임 구조로 만든 단기 예보 API 호출 테스트")
    public void KMAApiShortTermTest() throws Exception {

        KMAApiService kmaApiService = new KMAAPIShortTerm();

        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 단기예보 호출을 위한 알맞은 시간 가져오기
        String requestTime = "0200";

        WeatherDataDTO requestedWeatherDTO = kmaApiService.requestWeatherApi(requestDate, requestTime);
        System.out.println(requestedWeatherDTO.toString());

    }
}
