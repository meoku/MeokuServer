package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubMenuDetailsRepository extends JpaRepository<SubMenuDetails, Integer> {
    List<SubMenuDetails> findBySubDailyMenu(SubDailyMenu subDailyMenu);
}
