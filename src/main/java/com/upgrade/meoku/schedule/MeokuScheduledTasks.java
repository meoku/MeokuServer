package com.upgrade.meoku.schedule;

import com.upgrade.meoku.weather.MeokuWeatherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MeokuScheduledTasks {

    private final MeokuWeatherService meokuWeatherService;

    public MeokuScheduledTasks(MeokuWeatherService meokuWeatherService) {
        this.meokuWeatherService = meokuWeatherService;
    }
    //기상청 API - 초단기 실황 정보 가져오기실행
    @Scheduled(cron = "0 41 * * * *") // API 정보 업데이트는 매시간 40분 부터이므로 41분 API 호출
    public void insertUltraShortTermCurrentConditions() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        meokuWeatherService.insertWeatherDataFromApi();
    }
    //기상청 API - 단기예보 실행
    @Scheduled(cron = "0 2,5,8,11,14,17,20,23 11 * * *")
    public void insertShortTermForecast() {
    }


}
