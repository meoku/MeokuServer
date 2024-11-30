package com.upgrade.meoku.schedule;

import com.upgrade.meoku.mealmenu.data.dao.SubMenuDao;
import com.upgrade.meoku.mealmenu.util.MenuUtil;
import com.upgrade.meoku.menuOrder.MeokuMealOrderDTO;
import com.upgrade.meoku.menuOrder.MeokuMealOrderService;
import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.MeokuWeatherService;
import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.service.KMAAPIShortTerm;
import com.upgrade.meoku.weather.api.service.KMAAPIUltraShortTerm;
import com.upgrade.meoku.weather.api.service.KMAApiPerTemp;
import com.upgrade.meoku.weather.api.service.KMAApiUVIndex;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Component
public class MeokuScheduledTasks {
    //날씨 관련
    private final MeokuWeatherService meokuWeatherService;
    private final KMAAPIShortTerm kmaapiShortTerm;
    private final KMAAPIUltraShortTerm kmaapiUltraShortTerm;
    private final KMAApiUVIndex kmaApiUVIndex;
    private final KMAApiPerTemp kmaApiPerTemp;
    //배식순서 관련
    private final MeokuMealOrderService mealOrderService;

    private final SubMenuDao subMenuDao;

    public MeokuScheduledTasks(MeokuWeatherService meokuWeatherService,
                               KMAAPIShortTerm kmaapiShortTerm,
                               KMAAPIUltraShortTerm kmaapiUltraShortTerm,
                               KMAApiUVIndex kmaApiUVIndex,
                               KMAApiPerTemp kmaApiPerTemp,
                               MeokuMealOrderService mealOrderService, SubMenuDao subMenuDao) {
        this.meokuWeatherService = meokuWeatherService;
        this.kmaapiShortTerm = kmaapiShortTerm;
        this.kmaapiUltraShortTerm = kmaapiUltraShortTerm;
        this.kmaApiUVIndex = kmaApiUVIndex;
        this.kmaApiPerTemp = kmaApiPerTemp;
        this.mealOrderService = mealOrderService;
        this.subMenuDao = subMenuDao;
    }
    //매일 00:01 만료된 태그 삭제
    @Scheduled(cron = "0 1 0 * * *")
    public void deleteExpiredTag() throws Exception{
        Timestamp curDate = MenuUtil.getCurrentTimestamp();
        Long deleteCnt = subMenuDao.deleteExpiredMenuTag(curDate);
        System.out.println(deleteCnt);
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
    //기상청 API - 자외선 지수 + 체감온도 0시부터 3시간간격으로 데이터 나오는데 아침부터 밤까지 만 실행되게
    @Scheduled(cron = "0 1 6,9,12,15,18,21 * * *")
    public void insertUVIndex() throws Exception {
        System.out.println("스케줄러가 실행되었습니다: " + java.time.LocalDateTime.now());
        LocalDate targetDate = LocalDate.now();

        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTimeToShort();

        int errorResult = 0;
        try {
            System.out.println("자외선 API 실행");
            WeatherDataDTO UVIndexDataDTO = kmaApiUVIndex.requestWeatherApi(requestDate, requestTime);
            WeatherData newUpdatedWeatherData1 = meokuWeatherService.updateWeatherDataFromApi(targetDate, UVIndexDataDTO);
        }catch (Exception e){
            errorResult++;
            e.printStackTrace();
        }

        WeatherData newUpdatedWeatherData2 = new WeatherData();
        try {
            System.out.println("체감온도 API 실행");
            WeatherDataDTO percivedTempDataDTO = kmaApiPerTemp.requestWeatherApi(requestDate, requestTime);
            if (percivedTempDataDTO != null) {
                newUpdatedWeatherData2 = meokuWeatherService.updateWeatherDataFromApi(targetDate, percivedTempDataDTO);
            }else{
                System.out.println("체감온도를 가져올 수 없습니다. (겨울철은 체감온도 제공X)");
            }

        }catch (Exception e){
            errorResult++;
            e.printStackTrace();
        }

        System.out.println("스케줄러가 종료되었습니다: " + java.time.LocalDateTime.now());
        if (errorResult == 0) {System.out.println("저장된 날씨 데이터: \n" + newUpdatedWeatherData2.toString());}
        else{System.out.println("에러 발생했습니다.");}

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
