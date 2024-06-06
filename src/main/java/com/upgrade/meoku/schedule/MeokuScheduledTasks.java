package com.upgrade.meoku.schedule;

import com.upgrade.meoku.util.RequestApiUtil;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MeokuScheduledTasks {

    private final MeokuWeatherService meokuWeatherService;

    public MeokuScheduledTasks(MeokuWeatherService meokuWeatherService) {
        this.meokuWeatherService = meokuWeatherService;
    }

    @Scheduled(cron = "0 1 * * * *") // 매 정각 1분마다 실행
    public void insertWeatherDataFromApi() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        meokuWeatherService.insertWeatherDataFromApi();
    }

}
