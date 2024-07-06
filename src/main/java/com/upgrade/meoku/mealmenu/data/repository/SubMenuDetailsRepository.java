package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubMenuDetailsRepository extends JpaRepository<SubMenuDetails, Integer> {
}
