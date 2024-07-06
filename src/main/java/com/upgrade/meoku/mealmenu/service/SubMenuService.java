package com.upgrade.meoku.mealmenu.service;

import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubMenuService {
    public List<SubDailyMenuDTO> MenuImageUploadAndReturnMenuData(MultipartFile menuFile) throws Exception;
}
