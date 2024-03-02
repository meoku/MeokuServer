package com.upgrade.meoku.menuOrder;

import com.upgrade.meoku.data.entity.MeokuMenuTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeokuMealOrderRepository extends JpaRepository<MeokuMealOrder, Integer> {
    List<MeokuMealOrder> findByMeokuMealOrderGroupMealOrderGroupId(Integer mealOrderGroupId);// 배식그룹Id로 배식순서 데이터 가져오기
    // 배식순서 데이터 저장
}
