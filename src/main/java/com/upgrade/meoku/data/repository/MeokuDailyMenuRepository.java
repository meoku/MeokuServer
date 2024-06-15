package com.upgrade.meoku.data.repository;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MeokuDailyMenuRepository extends JpaRepository<MeokuDailyMenu, Integer> {
    List<MeokuDailyMenu> findByMenuDateBetween(Timestamp startDate, Timestamp endDate);
}