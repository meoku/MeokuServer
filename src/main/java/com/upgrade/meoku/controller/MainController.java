package com.upgrade.meoku.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.schedule.MeokuScheduledTasks;
import com.upgrade.meoku.schedule.MeokuWeatherService;
import com.upgrade.meoku.schedule.WeatherData;
import com.upgrade.meoku.schedule.WeatherDataDTO;
import com.upgrade.meoku.service.MainService;
import com.upgrade.meoku.util.RequestApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Main화면 컨트롤러", description = "메인화면 기능")
@RestController
@RequestMapping("/api/v1/meoku")
public class MainController {

    private final MainService mainService;
    private final RequestApiUtil requestApiUtil;
    private final MeokuWeatherService meokuWeatherService;

    @Autowired
    public MainController(MainService mainService,
                          RequestApiUtil requestApiUtil, MeokuWeatherService meokuWeatherService){
        this.mainService= mainService;
        this.requestApiUtil = requestApiUtil;
        this.meokuWeatherService = meokuWeatherService;
    }

    @Operation(summary = "서버동작 확인", description = "Just Print Hello World")
    @GetMapping(value = "helloworld")
    public String HelloWorld() {
        return "HelloWorld";
    }

    @Operation(summary = "주간별 식단메뉴 불러오기", description = "한주에 속하는 날짜를 입력하면 해당 주간의 식단을 가져옵니다. \n 입력 예제 {isMonthOrWeek : [week or month], date : YYYY-mm-dd}")
    @PostMapping(value = "weekdaysmenu")
    public List<MeokuDailyMenuDTO> getWeekendMealMenu(@RequestBody Map<String, Object> jsonData) {
//    @RequestParam("isMonthOrWeek")String isMonthOrWeekend,
//    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date //@DateTimeFormat는 일반적으로 yyyy-MM-dd형태

        String isMonthOrWeek = (String)jsonData.get("isMonthOrWeek");
        String date = (String)jsonData.get("date");
        LocalDate transDate = LocalDate.parse(date);

        List<MeokuDailyMenuDTO> resultMealMenuList = new ArrayList<>();

        if(isMonthOrWeek.equals("week")){
            resultMealMenuList = mainService.searchDailyMenuOfWeekDays(transDate);
        }else if(isMonthOrWeek.equals("month")){
            //월별은 추후 예정
        }

        return resultMealMenuList;
    }

    @Operation(summary = "날씨 데이터 가져오기", description = "기상청 API 호출로 구현 (1000번 제한있으므로 추후 개선 필요)")
    @GetMapping(value = "/getCurrentWeatherData")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentWeatherData() {
        Map<String, Object> responseBody = new HashMap<>();
        try{
            WeatherDataDTO weatherDataDTO = meokuWeatherService.getWeatherDataFromApi();
            responseBody.put("responseBody", weatherDataDTO);
        }catch (Exception e) {
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "날씨 데이터 가져와 저장", description = "수동으로 직접 날씨 데이터 저장")
    @GetMapping(value = "/insertCurrentWeatherData")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> insertCurrentWeatherData() {
        Map<String, Object> responseBody = new HashMap<>();
        try{
            WeatherData weatherData = meokuWeatherService.insertWeatherDataFromApi();
            responseBody.put("responseBody", weatherData);
        }catch (Exception e) {
            responseBody.put("error", "API 재대로 실행 안됨");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


}
