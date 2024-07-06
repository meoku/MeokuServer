package com.upgrade.meoku.mealmenu.data.dao;


import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class SubMenuDaoImpl implements SubMenuDao{
    @Override
    public void insertDailyMenu(MeokuDailyMenu dailyMenu) {

    }

    @Override
    public void insertDetailedMenu(List<MeokuDetailedMenu> detailedMenuList) {

    }

    @Override
    public void inserMenuDetail(MeokuMenuDetail meokuMenuDetail) {

    }

    @Override
    public void insertMenuDetailList(List<MeokuMenuDetail> menuDetailList) {

    }

    @Override
    public MeokuMenuDetail searchMenuDetailAndSave(String menuName) {
        return null;
    }

    @Override
    public List<MeokuDailyMenuDTO> searchDailyMenuOfWeekDays(Timestamp startDate, Timestamp endDate) {
        return null;
    }
}
