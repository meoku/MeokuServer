package com.upgrade.meoku.service;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;

import java.time.LocalDate;
import java.util.List;

public interface MainService {
    //startDate ~  endDate 입력받고 해당 주간 DailyMenu가져오기
    public List<MeokuDailyMenuDTO> searchDailyMenuOfWeekDays(LocalDate date);
}
