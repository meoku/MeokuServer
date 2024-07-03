package com.upgrade.meoku.schedule;

import com.upgrade.meoku.menuOrder.MeokuMealOrderDTO;
import com.upgrade.meoku.menuOrder.MeokuMealOrderService;
import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.MeokuWeatherService;
import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.KMAAPIShortTerm;
import com.upgrade.meoku.weather.api.KMAAPIUltraShortTerm;
import com.upgrade.meoku.weather.api.KMAApiUVIndex;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MeokuScheduledTasks {
    //날씨 관련
    private final MeokuWeatherService meokuWeatherService;
    private final KMAAPIShortTerm kmaapiShortTerm;
    private final KMAAPIUltraShortTerm kmaapiUltraShortTerm;
    private final KMAApiUVIndex kmaApiUVIndex
    //배식순서 관련
    private final MeokuMealOrderService mealOrderService;

    public MeokuScheduledTasks(MeokuWeatherService meokuWeatherService,
                               KMAAPIShortTerm kmaapiShortTerm,
                               KMAAPIUltraShortTerm kmaapiUltraShortTerm,
                               KMAApiUVIndex kmaApiUVIndex,
                               MeokuMealOrderService mealOrderService) {
        this.meokuWeatherService = meokuWeatherService;
        this.kmaapiShortTerm = kmaapiShortTerm;
        this.kmaapiUltraShortTerm = kmaapiUltraShortTerm;
        this.kmaApiUVIndex = kmaApiUVIndex;
        this.mealOrderService = mealOrderService;
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
    @Scheduled(cron = "0 11 2,5,8,11,14,17,20,23 * * *")
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
    //기상청 API - 자외선 지수 0시부터 3시간간격으로 데이터 나오는데 아침부터 밤까지 만 실행되게
    @Scheduled(cron = "0 1 6,9,12,15,18,21 * * *")
    public void insertUVIndex() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        LocalDate targetDate = LocalDate.now();

        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTime();

        WeatherDataDTO newWeatherDataDTO = kmaApiUVIndex.requestWeatherApi(requestDate, requestTime);
        WeatherData newUpdatedWeatherData = meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO);

        System.out.println("스케줄러가 종료되었습니다: " + java.time.LocalDateTime.now());
        System.out.println("저장된 날씨 데이터: \n" + newUpdatedWeatherData.toString());
    }

    @Scheduled(cron = "0 0 0 * * SAT") // 매주 토요일 00시
    public void runScheduledTask() {
        System.out.println("매주 토요일 00시에 배식순서 데이터 추가");
        List<MeokuMealOrderDTO> savedOrderDTOList = null;
        try {
            savedOrderDTOList = mealOrderService.saveWeeklyMealOrderDataByLatestData();
        } catch (Exception e) {
            System.out.println("저장에 사용할 기존 데이터가 없습니다.");
            throw new RuntimeException(e);
        }
        System.out.println("저장된 배식 순서" + savedOrderDTOList.toArray());
    }

}
