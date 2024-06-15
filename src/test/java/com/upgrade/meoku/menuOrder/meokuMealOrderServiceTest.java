package com.upgrade.meoku.menuOrder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class meokuMealOrderServiceTest {

    @Autowired
    private MeokuMealOrderService meokuMealOrderService;

    @Autowired
    private MeokuMealOrderDao meokuMealOrderDao;

    @Test
    @DisplayName("초기 데이터 저장 -> 서비스 시작 이후로는 사용할 일 없음;")
    @Disabled
    public void addMeokuMealOrderData(){
        // Group Entity 생성
        MeokuMealOrderGroup meokuMealOrderGroup = new MeokuMealOrderGroup();
        meokuMealOrderGroup.setMealOrderStartDate(LocalDate.of(2024,3,4));
        meokuMealOrderGroup.setMealOrderEndDate(LocalDate.of(2024,3,8));

        // Order Entity 생성
        List<MeokuMealOrder> orderList = new ArrayList<>();

        MeokuMealOrder order1 = new MeokuMealOrder();
        MeokuMealOrder order2 = new MeokuMealOrder();
        MeokuMealOrder order3 = new MeokuMealOrder();

        order1.setMeokuMealOrderGroup(meokuMealOrderGroup);
        order2.setMeokuMealOrderGroup(meokuMealOrderGroup);
        order3.setMeokuMealOrderGroup(meokuMealOrderGroup);

        order1.setMealOrder(1);
        order2.setMealOrder(2);
        order3.setMealOrder(3);

        order1.setMealTarget("2층");
        order2.setMealTarget("4,5,6층");
        order3.setMealTarget("1,3층");

        order1.setMealTime(Time.valueOf("1970-01-01 11:30:00"));
        order2.setMealTime(Time.valueOf("1970-01-01 12:00:00"));
        order3.setMealTime(Time.valueOf("1970-01-01 12:30:00"));

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        // Group, Order 순서대로 저장
        meokuMealOrderDao.saveMealOrderGroupData(meokuMealOrderGroup);
        meokuMealOrderDao.saveMealOrders(orderList);

        // 확인
        Optional<MeokuMealOrderGroup> orderGroup = meokuMealOrderDao.findLatestMealOrderGroup();
        int groupId = orderGroup.get().getMealOrderGroupId();
        List<MeokuMealOrder> savedOrderData = meokuMealOrderDao.findMealOrdersByGroupId(groupId);
        System.out.println(groupId + "");
        System.out.println(savedOrderData.toString());
    }

    @Test
    @DisplayName("다음주 시작, 종료날짜 추출하는 메소드 Test")
    public void testNextStartAndEndDays(){
        // 임의의 날짜와 시간 설정
        int year = 2024;
        int month = 3;
        int day = 24;
        int hour = 0;
        int minute = 0;
        int second = 0;

        // LocalDateTime 생성
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        // LocalDateTime을 Timestamp로 변환
//        LocalDate localDate = LocalDate.parse(localDateTime);
//        System.out.println(meokuMealOrderService.getNextWeekStartAndEndDate(timestamp).toString());
    }


}
