package com.upgrade.meoku.menuOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    //가장 최신 배식그룹ID 가져오기
    public Integer findLatestMealOrderGroupId(){
        MeokuMealOrderGroup latestGroup = meokuMealOrderGroupRepository.findTopByOrderByMealOrderGroupId();
        return latestGroup.getMealOrderGroupId();
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
    public void saveMealOrders(List<MeokuMealOrder> mealOrders){
        meokuMealOrderRepository.saveAll(mealOrders);
    }


}
