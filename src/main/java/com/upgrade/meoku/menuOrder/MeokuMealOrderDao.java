package com.upgrade.meoku.menuOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MeokuMealOrderDao {
    private final MeokuMealOrderGroupRepository meokuMealOrderGroupRepository;
    private final MeokuMealOrderRepository meokuMealOrderRepository;

    @Autowired
    public MeokuMealOrderDao(MeokuMealOrderGroupRepository meokuMealOrderGroupRepository, MeokuMealOrderRepository meokuMealOrderRepository) {
        this.meokuMealOrderGroupRepository = meokuMealOrderGroupRepository;
        this.meokuMealOrderRepository = meokuMealOrderRepository;
    }

    /* MeokuMealOrderGroup 관련 */
    public Optional<MeokuMealOrderGroup> findMealOrderGroupsByDate(LocalDate date) {
        return meokuMealOrderGroupRepository.findByMealOrderStartDateLessThanEqualAndMealOrderEndDateGreaterThanEqual(date, date);
    }

    //가장 최신 배식그룹 Data 가져오기
    public Optional<MeokuMealOrderGroup> findLatestMealOrderGroup(){
        Optional<MeokuMealOrderGroup> latestGroup = meokuMealOrderGroupRepository.findTopByOrderByMealOrderGroupIdDesc();
        return latestGroup;
    }

    //배식그룹 데이터 저장
    public void saveMealOrderGroupData(MeokuMealOrderGroup mealOrderGroup){
        meokuMealOrderGroupRepository.save(mealOrderGroup);
    }

    /* MeokuMealOrder 관련 */
    // 배식그룹Id로 배식순서 데이터 가져오기
    public List<MeokuMealOrder> findMealOrdersByGroupId(Integer groupId){
        return meokuMealOrderRepository.findByMeokuMealOrderGroupMealOrderGroupId(groupId);
    }

    // 배식순서 데이터 저장
    public List<MeokuMealOrderDTO> saveMealOrders(List<MeokuMealOrder> mealOrders){
        List<MeokuMealOrder> savedMealOrderList =  meokuMealOrderRepository.saveAll(mealOrders);
        List<MeokuMealOrderDTO> savedMealOrderDTOList = new ArrayList<>();
        for( MeokuMealOrder mealOrder : savedMealOrderList ){
            MeokuMealOrderDTO savedMealOrderDTO = new MeokuMealOrderDTO();
            savedMealOrderDTO.setMealOrderId(mealOrder.getMealOrderId());
            savedMealOrderDTO.setMealTarget(mealOrder.getMealTarget());
            savedMealOrderDTO.setMealTime(mealOrder.getMealTime());

            savedMealOrderDTOList.add(savedMealOrderDTO);
        }

        return savedMealOrderDTOList;
    }


}
