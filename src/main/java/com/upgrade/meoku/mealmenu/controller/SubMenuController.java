package com.upgrade.meoku.mealmenu.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuItemDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import com.upgrade.meoku.mealmenu.service.SubMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "식단 컨트롤러", description = "식단과 관련된 모든 요청")
@RestController
@RequestMapping("/api/v1/meokumenu")
public class SubMenuController {

    private final SubMenuService subMenuService;

    @Autowired
    public SubMenuController(SubMenuService subMenuService) {
        this.subMenuService = subMenuService;
    }


    @Operation(summary = "new 주간별 식단메뉴 불러오기 -  리팩터링", description = "한주에 속하는 날짜를 입력하면 해당 주간의 식단을 가져옵니다. \n 입력 예제 {date : YYYY-mm-dd}")
    @PostMapping(value = "weekdaysmenu")
    public List<SubDailyMenuDTO> getWeekendMealMenu(@RequestBody Map<String, Object> jsonData) {
        //String isMonthOrWeek = (String)jsonData.get("isMonthOrWeek");
        String date = (String)jsonData.get("date");
        LocalDate transDate = LocalDate.parse(date);

        List<SubDailyMenuDTO> resultMealMenuList = null;
        resultMealMenuList = subMenuService.searchDailyMenuOfWeek(transDate);

        return resultMealMenuList;
    }

    @Operation(summary = "메뉴id List로 해당 메뉴 태그정보 가져오기", description = "List형태로 MenuId를 \"menuIdList\" 변수에 넣어 전달")
    @PostMapping(value = "searchMenuTag")
    public List<SubMenuItemDTO> searchMenuTag(@RequestBody Map<String, Object> jsonData) {
        List<Integer> menuIdList = (List<Integer>)jsonData.get("menuIdList");

        return subMenuService.searchMenuTag(menuIdList);
    }

    @Operation(summary = "new 식단 이미지 OCR - 리팩터링", description = "업로드된 이미지 or 파일을 받아 API를 이용해 식단 데이터를 반환한다")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/MenuImageUploadAndReturnMenuData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public List<SubDailyMenuDTO> MenuImageUploadAndReturnMenuData(@RequestParam("menuFile") MultipartFile menuFile) throws Exception {
        // 업로드된 파일 없으면 임시로 null 값
        if (menuFile.isEmpty()) {return null;}
        //이미지 -> 식단 데이터 로직
        List<SubDailyMenuDTO> menuData = subMenuService.MenuImageUploadAndReturnMenuData(menuFile);
        return menuData;
    }

    @Operation(summary = "new 주간 식단데이터 업로드 - 리팩터링", description = "업로드된 이미지에서 추출한 식단데이터를 확인 후 서버DB로 저장 후 저장 날짜 반환")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/WeekMenuUpload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> WeekMenuUpload(@RequestBody List<SubDailyMenuDTO> weekMenu){
        Map<String, Object> responseBody = new HashMap<>();
        try{
            //식단 정보 저장
            List<String> isSavedDateList= subMenuService.WeekMenuUpload(weekMenu);
            responseBody.put("SavedDateList", isSavedDateList);
        }catch (Exception e) {
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "특정 날짜식단정보 모두 삭제", description = "입력예시 \"date\" : \"2024-07-31\"")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "deleteMenuData")
    public ResponseEntity<Map<String, Object>> deleteMenuData(@RequestBody Map<String, Object> jsonData) {
        String date = (String)jsonData.get("date");
        LocalDate deleteDate = LocalDate.parse(date);

        Map<String, Object> responseBody = new HashMap<>();
        try{
            //식단 정보 저장
            subMenuService.deleteMenuData(deleteDate);
            responseBody.put("result", date + "삭제 성공");
        }catch (Exception e) {
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }



        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
