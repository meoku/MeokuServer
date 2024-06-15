package com.upgrade.meoku.menuOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MeokuMealOrderGroupRepository extends JpaRepository<MeokuMealOrderGroup, Integer> {

    Optional<MeokuMealOrderGroup> findByMealOrderStartDateLessThanEqualAndMealOrderEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);//입력 받은 날짜가 해당하는 배식 Group 가져오기
    Optional<MeokuMealOrderGroup> findTopByOrderByMealOrderGroupIdDesc(); // 직전 배식 그룹 ID 가져오기
    //배식그룹 데이터 저장 -> 부모 save
}
