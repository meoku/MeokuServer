package com.upgrade.meoku.weather;

import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.api.KMAAPIShortTerm;
import com.upgrade.meoku.weather.api.KMAAPIUltraShortTerm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class MeokuWeatherAPIServiceTest {

    @Autowired
    private MeokuWeatherAPIService meokuWeatherAPIService;
    @Autowired
    private MeokuWeatherService meokuWeatherService;

    @Autowired
    private KMAAPIUltraShortTerm kmaAPIUltraShortTerm;
    @Autowired
    private KMAAPIShortTerm kmaAPIShortTerm;

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

    @Test
    @DisplayName("스케쥴러에서 사용할 로직 그대로 사용해보기")
    public void test() throws Exception {
        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 현재 시간 가져오기
        String requestTime = RequestApiUtil.getCurrentTime();
        String requestTimeForShortTerm = RequestApiUtil.getRequestTimeForShortTermForecastRequest();
        LocalDate targetDate = LocalDate.now();

        WeatherDataDTO newWeatherDataDTO =  kmaAPIUltraShortTerm.requestWeatherApi(requestDate, requestTime);
//        WeatherDataDTO newWeatherDataDTO =  kmaAPIShortTerm.requestWeatherApi(requestDate, requestTimeForShortTerm);
        meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO);
    }
}
