package com.upgrade.meoku.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "관리자 관련 컨트롤러", description = "Meoku 관리를 위한 관리자 요청")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService= adminService;
    }

    @Operation(summary = "health check")
    @GetMapping(value = "/healthCheck")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> healthCheck() throws Exception {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("responseBody", LocalDateTime.now().toString());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "관리자 계정여부 확인", description = "토큰 이 없다면 401 에러, 정상적인 토근을 가지고 있지만 권한이 없다면 500에러")
    @GetMapping(value = "/getAdminYn")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("관리자 입니다.");
    }


//    @Operation(summary = "식단 이미지 OCR", description = "업로드된 이미지 or 파일을 받아 API를 이용해 식단 데이터를 반환한다")
//    @PostMapping(value = "/MenuImageUploadAndReturnMenuData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseBody
//    public List<MeokuDailyMenuDTO> MenuImageUploadAndReturnMenuData(@RequestParam("menuFile") MultipartFile menuFile) throws Exception {
//        // 업로드된 파일 없으면 임시로 null 값
//        if (menuFile.isEmpty()) {return null;}
//        //이미지 -> 식단 데이터 로직
//        List<MeokuDailyMenuDTO> menuData = adminService.MenuImageUploadAndReturnMenuData(menuFile);
//        return menuData;
//    }
//
//    @Operation(summary = "주간 식단데이터 업로드", description = "업로드된 이미지에서 추출한 식단데이터를 확인 후 서버DB로 저장")
//    @PostMapping(value = "/WeekMenuUpload")
//    @ResponseBody
//    public String WeekMenuUpload(@RequestBody List<MeokuDailyMenuDTO> weekMenu){
//        //식단 정보 저장
//        adminService.WeekMenuUpload(weekMenu);
//        return "Success";
//    }

}
