package com.upgrade.meoku.menuOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    //이번주 배식 순서
    public List<MeokuMealOrder> findThisWeekMealOrder(LocalDate requestDate){
        Optional<MeokuMealOrderGroup> finedOrderGroup = meokuMealOrderDao.findMealOrderGroupsByDate(requestDate);
        // 해당날짜로 데이터가 배식순서 데이터가 존재하지 않으면 null값 반환
        if(!finedOrderGroup.isPresent()) return null;

        List<MeokuMealOrder> findedOrderList = meokuMealOrderDao.findMealOrdersByGroupId(finedOrderGroup.get().getMealOrderGroupId());
        return findedOrderList;
    }

    // 직접 데이터 저장
    @Transactional
    public List<MeokuMealOrderDTO> saveWeeklyMealOrderData(Map<String, Object> jsonData){
        // 입력받은 데이터 정렬
        String startDate = (String)jsonData.get("startDate");
        String endDate = (String)jsonData.get("endDate");

        LocalDate startDateTimestamp = LocalDate.parse(startDate);
        LocalDate endDateTimestamp = LocalDate.parse(startDate);

        MeokuMealOrderGroup newMealOrderGroup = new MeokuMealOrderGroup();
        newMealOrderGroup.setMealOrderStartDate(startDateTimestamp);
        newMealOrderGroup.setMealOrderEndDate(endDateTimestamp);

        List<MeokuMealOrder> newMealOrderList = new ArrayList<>();

        List<Map<String, Object>> mealOrderListData = (List<Map<String, Object>>) jsonData.get("mealOrderListData");
        for (Map<String, Object> data : mealOrderListData) {
            // 필요한 필드를 추출하여 A 객체 생성
            MeokuMealOrder mealOrder = new MeokuMealOrder();
            mealOrder.setMeokuMealOrderGroup(newMealOrderGroup);
            mealOrder.setMealOrder((Integer) data.get("mealOrder"));
            mealOrder.setMealTarget((String) data.get("mealTarget"));
            mealOrder.setMealTime(Time.valueOf((String)data.get("mealTime")));

            newMealOrderList.add(mealOrder);
        }

        //데이터 저장
        meokuMealOrderDao.saveMealOrderGroupData(newMealOrderGroup);
        List<MeokuMealOrderDTO> savedMealOrderDTOList = meokuMealOrderDao.saveMealOrders(newMealOrderList);

        return savedMealOrderDTOList;

    }
    // 이전 데이터를 이용한 새로운 배식 순서 Data 저장
    @Transactional
    public List<MeokuMealOrderDTO> saveWeeklyMealOrderDataByLatestData() throws Exception {

        /* 저장에 필요한 Data 준비 */
        Optional<MeokuMealOrderGroup> latestMealOrderGroup = meokuMealOrderDao.findLatestMealOrderGroup();
        //혹시라도 기존 배식 데이터가 없다면 오류 발생
        if(!latestMealOrderGroup.isPresent()) throw new Exception();

        int latestMealOrderGroupId = latestMealOrderGroup.get().getMealOrderGroupId();//최신 배식 순서 Id 가져오기
        List<LocalDate> nextWeeekStartAndEndDay = this.getNextWeekStartAndEndDate(latestMealOrderGroup.get().getMealOrderStartDate());// 다음 배식 순서 시작날짜, 종료날짜

        List<MeokuMealOrder> latestOrderList = meokuMealOrderDao.findMealOrdersByGroupId(latestMealOrderGroupId);

        //target 맨 앞을 맨 뒤로 변경 처리위한 전처리
        List<String> mealTargetList = new ArrayList<>();
        for(MeokuMealOrder mealOrder : latestOrderList){
            mealTargetList.add(mealOrder.getMealTarget());
        }

        //직전 주 순서에서 하나씩 앞순 서로 배치(맨앞은 맨 뒤로)
        String firstTarget = mealTargetList.remove(0);
        mealTargetList.add(firstTarget);

        MeokuMealOrderGroup savedOrderGroup = new MeokuMealOrderGroup();//저장할 OrderGroup Entity 생성
        //배식순서 시작, 종료 일자 넣기
        savedOrderGroup.setMealOrderStartDate(nextWeeekStartAndEndDay.get(0));
        savedOrderGroup.setMealOrderEndDate(nextWeeekStartAndEndDay.get(1));

        List<MeokuMealOrder> savedOrderDataList = new ArrayList<>();
        //위에서 직전 주 순서를 하나씩 앞으로 댕긴 List로 새로 저장될 순서 데이터 적용
        IntStream.range(0, latestOrderList.size())
                .forEach(i -> {
                    MeokuMealOrder latestOrderData = latestOrderList.get(i);

                    MeokuMealOrder savedOrder = new MeokuMealOrder();

                    savedOrder.setMeokuMealOrderGroup(savedOrderGroup);
                    savedOrder.setMealOrder(i+1);
                    savedOrder.setMealTarget(mealTargetList.get(i));
                    savedOrder.setMealTime(latestOrderData.getMealTime());

                    savedOrderDataList.add(savedOrder);
                });

        meokuMealOrderDao.saveMealOrderGroupData(savedOrderGroup);
        List<MeokuMealOrderDTO> savedMealOrderDTOList = meokuMealOrderDao.saveMealOrders(savedOrderDataList);

        return savedMealOrderDTOList;
    }

    // **** Util

    public List<LocalDate> getNextWeekStartAndEndDate(LocalDate givenDate){

        LocalDate startOfWeek = givenDate.with(DayOfWeek.MONDAY);       // 주의 시작일 (월요일)
        LocalDate startOfNextWeek = startOfWeek.plusWeeks(1);// 다음 주의 시작일 (월요일)
        LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);       // 해당 주의 일요일
        LocalDate endOfNextWeek = startOfNextWeek.with(DayOfWeek.SUNDAY);// 다음 주의 일요일

        List<LocalDate> resultLocalDateList = new ArrayList<>();
        resultLocalDateList.add(startOfNextWeek);
        resultLocalDateList.add(endOfNextWeek);

        return resultLocalDateList;
    }

}
