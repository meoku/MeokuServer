package com.upgrade.meoku.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.service.AdminService;
import com.upgrade.meoku.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "Main화면 컨트롤러", description = "메인화면 기능")
@RestController
@RequestMapping("/api/v1/meoku")
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService){
        this.mainService= mainService;
    }

    @Operation(summary = "서버동작 확인", description = "Just Print Hello World")
    @GetMapping(value = "helloworld")
    public String HelloWorld() {
        return "HelloWorld";
    }

    @Operation(summary = "주간별 식단메뉴 불러오기", description = "한주에 속하는 날짜를 입력하면 해당 주간의 식단을 가져옵니다.")
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

}
