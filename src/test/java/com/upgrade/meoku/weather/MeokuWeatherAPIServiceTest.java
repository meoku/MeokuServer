package com.upgrade.meoku.weather;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MeokuWeatherAPIServiceTest {

    @Autowired
    private MeokuWeatherAPIService meokuWeatherAPIService;

    @Test
    @DisplayName("초단기실황 API 테스트")
    public void requestUltraShortTermCurrentConditions() throws Exception {
        meokuWeatherAPIService.getUltraShortTermCurrentConditions();
    }

    @Test
    @DisplayName("단기예보 API 테스트")
    public void reuqestShortTermForecast() throws Exception {
        WeatherDataDTO weatherDataDTO = meokuWeatherAPIService.getShortTermForecast();
        System.out.println(weatherDataDTO.toString());
    }
}
