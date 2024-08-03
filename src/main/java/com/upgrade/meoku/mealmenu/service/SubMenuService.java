package com.upgrade.meoku.mealmenu.service;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuTag;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface SubMenuService {
    public List<SubDailyMenuDTO> MenuImageUploadAndReturnMenuData(MultipartFile menuFile) throws Exception;
    public List<String> WeekMenuUpload(List<SubDailyMenuDTO> weekMenu);
    public List<SubDailyMenuDTO> searchDailyMenuOfWeek(LocalDate date);
    public void deleteMenuData (LocalDate delDate);
}
