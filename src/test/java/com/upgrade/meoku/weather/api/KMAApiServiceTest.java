package com.upgrade.meoku.weather.api;

import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class KMAApiServiceTest {

    @Autowired
    KMAAPIUltraShortTerm kmaapiUltraShortTerm;

    @Autowired
    KMAAPIShortTerm kmaApiShortTermServices;

    @Test
    @DisplayName("위임 구조로 만든 초단기 실황 API 호출 테스트")
    public void KMAApiUltraShortTermTest() throws Exception {

        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 단기예보 호출을 위한 알맞은 시간 가져오기
        String requestTime = RequestApiUtil.getCurrentTime();

        WeatherDataDTO requestedWeatherDTO = kmaapiUltraShortTerm.requestWeatherApi(requestDate, requestTime);
        System.out.println(requestedWeatherDTO.toString());

    }


    @Test
    @DisplayName("위임 구조로 만든 단기 예보 API 호출 테스트")
    public void KMAApiShortTermTest() throws Exception {

        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 단기예보 호출을 위한 알맞은 시간 가져오기
        String requestTime = "0200";

        WeatherDataDTO requestedWeatherDTO = kmaApiShortTermServices.requestWeatherApi(requestDate, requestTime);
        System.out.println(requestedWeatherDTO.toString());

    }
}
