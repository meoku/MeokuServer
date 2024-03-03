package com.upgrade.meoku.menuOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    @Transactional
    public void saveWeeklyMealOrderDataByLatestData(){

        /* 저장에 필요한 Data 준비 */
        int latestMealOrderGroupId = meokuMealOrderDao.findLatestMealOrderGroupId();//최신 배식 순서 Id 가져오기
        List<MeokuMealOrder> latestOrderList = meokuMealOrderDao.findMealOrdersByGroupId(latestMealOrderGroupId);

        //직전 주 순서에서 하나씩 앞순 서로 배치(맨앞은 맨 뒤로)
        MeokuMealOrder firstElement = latestOrderList.remove(0);
        latestOrderList.add(firstElement);

        MeokuMealOrderGroup savedOrderGroup = new MeokuMealOrderGroup();//저장할 OrderGroup Entity 생성
        //배식순서 시작, 종료 일자 넣는 로직 필요(추후 작성 예정)

        List<MeokuMealOrder> savedOrderDataList = new ArrayList<>();
        //위에서 직전 주 순서를 하나씩 앞으로 댕긴 List로 새로 저장될 순서 데이터 적용
        IntStream.range(0, latestOrderList.size())
                .forEach(i -> {
                    MeokuMealOrder latestOrderData = latestOrderList.get(i);

                    MeokuMealOrder savedOrder = new MeokuMealOrder();

                    savedOrder.setMeokuMealOrderGroup(savedOrderGroup);
                    savedOrder.setMealOrder(i+1);
                    savedOrder.setMealTarget(latestOrderData.getMealTarget());
                    savedOrder.setMealTime(latestOrderData.getMealTime());

                    savedOrderDataList.add(savedOrder);
                });

        meokuMealOrderDao.saveMealOrderGroupData(savedOrderGroup);
        meokuMealOrderDao.saveMealOrders(savedOrderDataList);

    }

}
