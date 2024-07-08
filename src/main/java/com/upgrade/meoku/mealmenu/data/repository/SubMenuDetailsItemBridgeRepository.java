package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetailsItemBridge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubMenuDetailsItemBridgeRepository extends JpaRepository<SubMenuDetailsItemBridge, Integer> {
    List<SubMenuDetailsItemBridge> findBySubMenuDetails(SubMenuDetails subMenuDetails);
}
