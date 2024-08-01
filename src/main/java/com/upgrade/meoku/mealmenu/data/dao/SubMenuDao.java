package com.upgrade.meoku.mealmenu.data.dao;

import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.entity.*;

import java.sql.Timestamp;
import java.util.List;

public interface SubMenuDao {
    public SubDailyMenu insertDailyMenu(SubDailyMenu dailyMenu);

    public SubMenuDetails insertMenuDetails(SubMenuDetails menuDetails);        //상세식단 저장
    public void insertDetailedMenu(List<MeokuDetailedMenu> detailedMenuList);   //상세식단List 저장
    public void inserMenuDetail(MeokuMenuDetail meokuMenuDetail);               //메뉴정보 저장
    public void insertBridgeList(List<SubMenuDetailsItemBridge> bridgeList);    //bridge List 저장
    public void insertMenuDetailList(List<SubMenuDetails> menuDetailList);

    //Menu 이름대로 MenuDetail에서 찾기
    public SubMenuItem menuItemCountUpAndSave(String menuName);
    //startDate ~  endDate 입력받고 해당 주간 DailyMenu가져오기
    public SubDailyMenu searchDailyMenu(Timestamp searchDate);  //날짜로 일일 데이터 찾기
    public List<SubDailyMenuDTO> searchDailyMenuOfWeek(Timestamp startDate, Timestamp endDate);//주간 식단메뉴 가져오기
    public SubDailyMenuDTO searchDailyMenuOfDay(Timestamp searchDate);// 특정 날짜 메뉴데이터 가져오기
    public boolean deleteMenuData(Timestamp deleteDate); // 특정 날짜 메뉴데이터 삭제
    public SubMenuItem searchMenuItem(String menuName);
    public SubMenuTag insertMenuTag(SubMenuTag subMenuTag); //메뉴에 태그 저장
}
