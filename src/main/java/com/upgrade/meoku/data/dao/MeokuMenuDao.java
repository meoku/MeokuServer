package com.upgrade.meoku.data.dao;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;

import java.util.List;

public interface MeokuMenuDao {
    public void insertDailyMenu(MeokuDailyMenu dailyMenu);
    //Detailed Menu 저장
    public void insertDetailedMenu(List<MeokuDetailedMenu> detailedMenuList);
    //Menu Detail 저장
    public void inserMenuDetail(MeokuMenuDetail meokuMenuDetail);
    public void insertMenuDetailList(List<MeokuMenuDetail> menuDetailList);
    //Menu 이름대로 MenuDetail에서 찾기
    public MeokuMenuDetail searchMenuDetail(String menuName);
}
