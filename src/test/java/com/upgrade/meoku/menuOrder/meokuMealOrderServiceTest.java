package com.upgrade.meoku.menuOrder;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class meokuMealOrderServiceTest {

    @Autowired
    private MeokuMealOrderService meokuMealOrderService;

    @Autowired
    private MeokuMealOrderDao meokuMealOrderDao;

    @Test
    @DisplayName("초기 데이터 저장")
    @Disabled
    public void addMeokuMealOrderData(){
        // Group Entity 생성
        MeokuMealOrderGroup meokuMealOrderGroup = new MeokuMealOrderGroup();
        meokuMealOrderGroup.setMealOrderStartDate(Timestamp.valueOf(LocalDate.of(2024,3,4).atStartOfDay()));
        meokuMealOrderGroup.setMealOrderEndDate(Timestamp.valueOf(LocalDate.of(2024,3,8).atStartOfDay()));

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

        order1.setMealTime(Timestamp.valueOf("1970-01-01 11:30:00"));
        order2.setMealTime(Timestamp.valueOf("1970-01-01 12:00:00"));
        order3.setMealTime(Timestamp.valueOf("1970-01-01 12:30:00"));

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        // Group, Order 순서대로 저장
        meokuMealOrderDao.saveMealOrderGroupData(meokuMealOrderGroup);
        meokuMealOrderDao.saveMealOrders(orderList);

        // 확인
        int groupId = meokuMealOrderDao.findLatestMealOrderGroupId();
        List<MeokuMealOrder> savedOrderData = meokuMealOrderDao.findMealOrdersByGroupId(groupId);
        System.out.println(groupId + "");
        System.out.println(savedOrderData.toString());
    }

    @Test
    @DisplayName("최근 배식순서로 새로운 배식순서 Data 저장")
    public void confirmOrderDate(){
        meokuMealOrderService.saveWeeklyMealOrderDataByLatestData();
    }


}
