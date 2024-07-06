package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetailsItemBridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubMenuDetailsItemBridgeRepository extends JpaRepository<SubMenuDetailsItemBridge, Integer> {
}
