package com.upgrade.meoku.menuOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeokuMealOrderService {

    private final MeokuMealOrderDao meokuMealOrderDao;

    @Autowired
    public MeokuMealOrderService(MeokuMealOrderDao meokuMealOrderDao) {
        this.meokuMealOrderDao = meokuMealOrderDao;
    }


    // 한주간 새로운 배식순서 Data 저장
    public void saveWeeklyMealOrderData(){

    }

    // 이전 데이터를 이용한 새로운 배식 순서 Data 저장
    public void saveWeeklyMealOrderDataByLatestData(){

        int latestMealOrderGroupId = meokuMealOrderDao.findLatestMealOrderGroupId();//최신 배식 순서 Id 가져오기

        MeokuMealOrderGroup savedOrderGroup = new MeokuMealOrderGroup();//저장할 OrderGroup Entity 생성
        MeokuMealOrder savedOrder = new MeokuMealOrder();               //저장할 Order Entity 생성

    }

}
