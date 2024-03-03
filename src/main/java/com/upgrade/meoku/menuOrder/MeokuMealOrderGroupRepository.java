package com.upgrade.meoku.menuOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeokuMealOrderGroupRepository extends JpaRepository<MeokuMealOrderGroup, Integer> {

    MeokuMealOrderGroup findTopByOrderByMealOrderGroupId(); // 직전 배식 그룹 ID 가져오기
    //배식그룹 데이터 저장 -> 부모 save
}
