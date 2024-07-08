package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface SubDailyMenuRepository extends JpaRepository<SubDailyMenu, Integer> {

    Optional<SubDailyMenu> findByMenuDate(Timestamp searchDate);

    List<SubDailyMenu> findByMenuDateBetween(Timestamp startDate, Timestamp endDate);
}