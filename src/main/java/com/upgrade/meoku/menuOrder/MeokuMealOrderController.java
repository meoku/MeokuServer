package com.upgrade.meoku.menuOrder;

import com.upgrade.meoku.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "배식순서 관련 컨트롤러", description = "배식순서 정보 CRUD")
@RestController
@RequestMapping("/api/v1/mealOrder")
public class MeokuMealOrderController {
    private final MeokuMealOrderService mealOrderService;

    @Autowired
    public MeokuMealOrderController(MeokuMealOrderService mealOrderService){
        this.mealOrderService= mealOrderService;
    }

    @Operation(summary = "현 날짜로 해당 주의 배식 순서 가져오기")
    @GetMapping(value = "/findThisWeekMealOrder")
    @ResponseBody
    public List<MeokuMealOrder> findThisWeekMealOrder(@RequestParam("requestDate") String requestDate) {
        // ISO 8601 형식의 문자열을 LocalDateTime으로 변환

        Timestamp requestTimestamp = Timestamp.valueOf(requestDate);
        // 변환된 Timestamp를 이용하여 작업 수행
        List<MeokuMealOrder> findedMeokuOrderList =  mealOrderService.findThisWeekMealOrder(requestTimestamp);

        return findedMeokuOrderList;
    }



}
