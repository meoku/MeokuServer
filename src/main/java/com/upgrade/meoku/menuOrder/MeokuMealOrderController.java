package com.upgrade.meoku.menuOrder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "배식순서 관련 컨트롤러", description = "배식순서 정보 CRUD")
@RestController
@RequestMapping("/api/v1/mealOrder")
public class MeokuMealOrderController {
    private final MeokuMealOrderService mealOrderService;

    @Autowired
    public MeokuMealOrderController(MeokuMealOrderService mealOrderService){
        this.mealOrderService= mealOrderService;
    }

    @Operation(summary = "현 날짜로 해당 주의 배식 순서 가져오기 요청 데이터 Format : ex) 2024-05-06 -> 배식요청과 통일을위해 변경(20240608)")
    @PostMapping(value = "/findThisWeekMealOrder")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findThisWeekMealOrder(@RequestBody Map<String, String> jsonData) {
        Map<String, Object> responseBody = new HashMap<>();
        // ISO 8601 형식의 문자열을 LocalDateTime으로 변환
        //Timestamp requestTimestamp = Timestamp.valueOf(jsonData.get("requestDate"));
        LocalDate requestTimestamp = LocalDate.parse(jsonData.get("requestDate"));
        try{
            List<MeokuMealOrder> findedMeokuOrderList =  mealOrderService.findThisWeekMealOrder(requestTimestamp);
            responseBody.put("responseBody", findedMeokuOrderList);
        }catch (Exception e) {
            e.printStackTrace(); // 예외 스택 트레이스를 콘솔에 출력
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "현재 저장된 최산 배식순서 데이터로 다음 배식순서 데이터 저장")
    @GetMapping(value = "/saveMealOrderByLatestMealOrder")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveMealOrderByLatestMealOrder() {
        Map<String, Object> responseBody = new HashMap<>();
        try{
            List<MeokuMealOrderDTO> savedOrderDTOList = mealOrderService.saveWeeklyMealOrderDataByLatestData();
            responseBody.put("responseBody", savedOrderDTOList);
        }catch (Exception e) {
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "수동으로 직접 배식 정보 입력", description = "주의: startDate, endDate는 반드시 yyyy-mm-dd hh:mm:ss 형식으로 넣어야함 + mealOrder는 반드시 큰따옴표 빼고 정수형태로 넣어야함")
    @PostMapping(value = "/saveWeekMealOrder")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveWeekMealOrder(@RequestBody Map<String, Object> jsonData) {

        Map<String, Object> responseBody = new HashMap<>();
        try{
            List<MeokuMealOrderDTO> savedOrderDTOList = mealOrderService.saveWeeklyMealOrderData(jsonData);
            responseBody.put("responseBody", savedOrderDTOList);
        }catch (Exception e) {
            e.printStackTrace(); // 예외 스택 트레이스를 콘솔에 출력
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


}
