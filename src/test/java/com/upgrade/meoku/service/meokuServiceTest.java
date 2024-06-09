package com.upgrade.meoku.service;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.util.MeokuUtil;
import com.upgrade.meoku.weather.MeokuWeatherAPIService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class meokuServiceTest {

    @Autowired
    MainServiceImpl mainService;

    @Autowired
    MeokuWeatherAPIService meokuWeatherAPIService;

    @Test
    @DisplayName("날짜 입력받으면 해당 주간 평일 목록 반환 Service Method Test")
    @Disabled
    public void weekendTest(){
        LocalDate currentDate = LocalDate.now();

        List<LocalDate> weekdays = MeokuUtil.getWeekdaysInWeek(currentDate);
        // 예상되는 결과와 비교
        assertEquals(5, weekdays.size()); // 예상되는 평일 수는 5

        for(LocalDate date : weekdays){
            System.out.println(date.toString());

            //실제 평일인지 평가
            assert !date.getDayOfWeek().equals(DayOfWeek.SATURDAY);
            assert !date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        }
    }

    @Test
    @DisplayName("시작, 끝 날짜 입력으로 한주간 식단메뉴 가져오기")
    @Disabled
    public void getWeekDaysMealMenu(){
        LocalDate curDate = LocalDate.of(2024, 03, 25);

        List<MeokuDailyMenuDTO> weekDaysMenuList = mainService.searchDailyMenuOfWeekDays(curDate);

        // 예상되는 결과와 비교
        assertEquals(5, weekDaysMenuList.size()); // 예상되는 평일 수는 5

        for(MeokuDailyMenuDTO dailyMenuDTO : weekDaysMenuList){
            System.out.println(dailyMenuDTO.toString());
        }

    }

    @Test
    @DisplayName("날씨 API 테스트")
    public void getWhetherDataFromApi() throws Exception {
        System.out.println(meokuWeatherAPIService.getUltraShortTermCurrentConditions().toString());
    }
}
