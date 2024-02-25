package com.upgrade.meoku.service;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
    @Override
    public List<MeokuDailyMenuDTO> MenuImageUploadAndReturnMenu(MultipartFile menuFile) {
        return null;
    }
    @Override
    public void WeekMenuUpload(List<MeokuDailyMenuDTO> weekMenu) {

    }
}
