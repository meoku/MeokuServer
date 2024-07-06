package com.upgrade.meoku.mealmenu.controller;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.service.SubMenuService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SubMenuController {

    private final SubMenuService subMenuService;

    @Autowired
    public SubMenuController(SubMenuService subMenuService) {
        this.subMenuService = subMenuService;
    }


    @Operation(summary = "식단 이미지 OCR2", description = "업로드된 이미지 or 파일을 받아 API를 이용해 식단 데이터를 반환한다")
    @PostMapping(value = "/MenuImageUploadAndReturnMenuData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public List<SubDailyMenuDTO> MenuImageUploadAndReturnMenuData(@RequestParam("menuFile") MultipartFile menuFile) throws Exception {
        // 업로드된 파일 없으면 임시로 null 값
        if (menuFile.isEmpty()) {return null;}
        //이미지 -> 식단 데이터 로직
        List<SubDailyMenuDTO> menuData = subMenuService.MenuImageUploadAndReturnMenuData(menuFile);
        return menuData;
    }

}
