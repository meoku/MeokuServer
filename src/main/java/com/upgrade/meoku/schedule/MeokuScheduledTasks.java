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

    @Scheduled(cron = "0 41 * * * *") // API 정보 업데이트는 매시간 40분 부터이므로 41분 API 호출
    public void insertWeatherDataFromApi() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        meokuWeatherService.insertWeatherDataFromApi();
    }

}
