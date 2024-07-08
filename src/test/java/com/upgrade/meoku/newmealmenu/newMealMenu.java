package com.upgrade.meoku.newmealmenu;

import com.upgrade.meoku.mealmenu.data.dao.SubMenuDao;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsItemBridgeDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.mealmenu.data.repository.SubDailyMenuRepository;
import com.upgrade.meoku.util.MeokuUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class newMealMenu {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    SubDailyMenuRepository dailyMenuRepository;
    @Autowired
    SubMenuDao menuDao;


    @Test
    @DisplayName("주간 식단데이터 가져오기")
    public void searchDailyMenuOfWeekDays(){
        LocalDate date = LocalDate.of(2024, 07, 8);

        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(date);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));



        List<SubDailyMenuDTO> srchDm = menuDao.searchDailyMenuOfWeek(startTimestamp, endTimestamp);
        for(SubDailyMenuDTO dmd : srchDm){
            System.out.println(dmd.getMenuDate());
            for(SubMenuDetailsDTO mdd : dmd.getMenuDetailsList()){
                System.out.println(mdd.getMenuDetailsName() + ", Main Meal Yn : " + mdd.getMainMealYn());
                for(SubMenuDetailsItemBridgeDTO bd : mdd.getSubBridgeList()){
                    System.out.println(bd.getMenuItemName() + ", " + bd.getMainMenuYn());
                }
                System.out.println("");
            }
        }
    }

    @Test
    @DisplayName("저장된 식단데이터 가져오기 - 파기")
    public void searchDailyMenuOfWeek(){
        LocalDate date = LocalDate.of(2024, 07, 8);

        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(date);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        //일일메뉴, 상세식단 데이터까지 불러오기 (Hibernate의 bag 관련 이슈로 한번에 3개 테이블 의 데이터 못가져온다고 함)
        String jpql = "SELECT dm FROM SubDailyMenu dm " +
                "LEFT JOIN FETCH dm.menuDetailsList details " +
                "LEFT JOIN FETCH details.subBridgeList " +
                "WHERE dm.menuDate BETWEEN :startDate AND :endDate";

        List<SubDailyMenu> dailyMenus =  entityManager.createQuery(jpql, SubDailyMenu.class)
                .setParameter("startDate", startTimestamp)
                .setParameter("endDate", endTimestamp)
                .getResultList();

        for(SubDailyMenu dm : dailyMenus) {
            for (SubMenuDetails details : dm.getMenuDetailsList()) {
                Hibernate.initialize(details.getSubBridgeList());
            }
        }

        for(SubDailyMenu dm : dailyMenus) {
            System.out.println(dm.getDailyMenuId());
            for(SubMenuDetails md : dm.getMenuDetailsList()){
                System.out.println(md.getDailyMenuDate());
                System.out.println(md.getSubBridgeList());
            }
        }
    }
}
