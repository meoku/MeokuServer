package com.upgrade.meoku.schedule;

import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.MeokuWeatherService;
import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.KMAAPIShortTerm;
import com.upgrade.meoku.weather.api.KMAAPIUltraShortTerm;
import com.upgrade.meoku.weather.api.KMAApiShortTermResponseDTO;
import org.apache.coyote.Request;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.upgrade.meoku.util.RequestApiUtil.getRequestTimeForShortTermForecastRequest;

@Component
public class MeokuScheduledTasks {

    private final MeokuWeatherService meokuWeatherService;
    private final KMAAPIShortTerm kmaapiShortTerm;
    private final KMAAPIUltraShortTerm kmaapiUltraShortTerm;

    public MeokuScheduledTasks(MeokuWeatherService meokuWeatherService,
                               KMAAPIShortTerm kmaapiShortTerm,
                               KMAAPIUltraShortTerm kmaapiUltraShortTerm) {
        this.meokuWeatherService = meokuWeatherService;
        this.kmaapiShortTerm = kmaapiShortTerm;
        this.kmaapiUltraShortTerm = kmaapiUltraShortTerm;
    }
    //기상청 API - 초단기 실황 정보 가져오기실행
    @Scheduled(cron = "0 41 * * * *") // API 정보 업데이트는 매시간 40분 부터이므로 41분 API 호출
    public void insertUltraShortTermCurrentConditions() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        LocalDate targetDate = LocalDate.now();

        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTime();
        WeatherDataDTO newWeatherDataDTO = kmaapiUltraShortTerm.requestWeatherApi(requestDate, requestTime);
        WeatherData newUpdatedWeatherData = meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO);

        System.out.println("스케줄러가 종료되었습니다: " + java.time.LocalDateTime.now());
        System.out.println("저장된 날씨 데이터: \n" + newUpdatedWeatherData.toString());
    }
    //기상청 API - 단기예보 실행
    @Scheduled(cron = "0 2,5,8,11,14,17,20,23 11 * * *")
    public void insertShortTermForecast() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        LocalDate targetDate = LocalDate.now();

        String requestDate = RequestApiUtil.getTodayDate();
        String requestTimeForShortTerm = RequestApiUtil.getRequestTimeForShortTermForecastRequest();
        WeatherDataDTO newWeatherDataDTO = kmaapiShortTerm.requestWeatherApi(requestDate, requestTimeForShortTerm);
        WeatherData newUpdatedWeatherData = meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO);

        System.out.println("스케줄러가 종료되었습니다: " + java.time.LocalDateTime.now());
        System.out.println("저장된 날씨 데이터: \n" + newUpdatedWeatherData.toString());
    }

}
