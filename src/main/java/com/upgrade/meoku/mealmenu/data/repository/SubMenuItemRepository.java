package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubMenuItemRepository extends JpaRepository<SubMenuItem, Integer> {
    SubMenuItem findByMenuItemName(String menuDetailName);
    Optional<SubMenuItem> findByMenuItemId(Integer menuItemId);
}
