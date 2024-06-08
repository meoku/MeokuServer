package com.upgrade.meoku.service;

import com.upgrade.meoku.config.NaverCloudConfig;
import com.upgrade.meoku.data.dao.MeokuMenuDao;
import com.upgrade.meoku.data.dao.MeokuMenuDaoImpl;
import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.util.MeokuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainServiceImpl implements MainService{

    private final MeokuMenuDaoImpl meokuMenuDao;

    @Autowired
    public MainServiceImpl(MeokuMenuDaoImpl meokuMenuDao) {
        this.meokuMenuDao = meokuMenuDao;
    }

    //날짜 입력받고 해당 주간 DailyMenu가져오기
    public List<MeokuDailyMenuDTO> searchDailyMenuOfWeekDays(LocalDate date){
        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(date);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        return meokuMenuDao.searchDailyMenuOfWeekDays(startTimestamp, endTimestamp);
    }

}
