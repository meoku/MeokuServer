package com.upgrade.meoku.service;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {
    public List<MeokuDailyMenuDTO> MenuImageUploadAndReturnMenuData(MultipartFile menuFile) throws Exception; // image to menuData By OCR API
    public void WeekMenuUpload(List<MeokuDailyMenuDTO> weekMenu); // 주간 메뉴 저장
}
