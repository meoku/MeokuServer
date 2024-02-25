package com.upgrade.meoku.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "관리자 관련 컨트롤러", description = "Meoku 관리를 위한 관리자 요청")
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService= adminService;
    }

    @Operation(summary = "식단 이미지 OCR", description = "업로드된 이미지 or 파일을 받아 API를 이용해 식단 데이터를 반환한다")
    @PostMapping(value = "menuImageUploadAndReturnMenu")
    @ResponseBody
    public String MenuImageUploadAndReturnMenu(@RequestParam("menuFile") MultipartFile menuFile) {
        //이미지 -> 식단 데이터 로직
        return "JSON 파일";
    }

    @Operation(summary = "식단 이미지 OCR", description = "업로드된 이미지 or 파일을 받아 API를 이용해 식단 데이터를 반환한다")
    @PostMapping(value = "menuImageUploadAndReturnMenu")
    @ResponseBody
    public String WeekMenuUpload(@RequestParam List<MeokuDailyMenuDTO> weekMenu){
        //식단 정보 저장
        return "";
    }

}
