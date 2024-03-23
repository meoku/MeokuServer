package com.upgrade.meoku.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.exception.MeokuException;
import com.upgrade.meoku.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "관리자 관련 컨트롤러", description = "Meoku 관리를 위한 관리자 요청")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService= adminService;
    }

    @Operation(summary = "식단 이미지 OCR", description = "업로드된 이미지 or 파일을 받아 API를 이용해 식단 데이터를 반환한다")
    @PostMapping(value = "/MenuImageUploadAndReturnMenuData")
    @ResponseBody
    public List<MeokuDailyMenuDTO> MenuImageUploadAndReturnMenuData(@RequestParam("menuFile") MultipartFile menuFile) throws Exception {
        //이미지 -> 식단 데이터 로직
        List<MeokuDailyMenuDTO> menuData = adminService.MenuImageUploadAndReturnMenuData(menuFile);
        return menuData;
    }

    @Operation(summary = "주간 식단데이터 업로드", description = "업로드된 이미지에서 추출한 식단데이터를 확인 후 서버DB로 저장")
    @PostMapping(value = "/WeekMenuUpload")
    @ResponseBody
    public String WeekMenuUpload(@RequestBody List<MeokuDailyMenuDTO> weekMenu){
        //식단 정보 저장
        adminService.WeekMenuUpload(weekMenu);
        return "Success";
    }

}
