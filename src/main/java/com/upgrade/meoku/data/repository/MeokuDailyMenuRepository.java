package com.upgrade.meoku.data.repository;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeokuDailyMenuRepository extends JpaRepository<MeokuDailyMenu, Integer> {
}