package com.upgrade.meoku.data.repository;

import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeokuMenuDetailRepository extends JpaRepository<MeokuMenuDetail, Integer> {
    MeokuMenuDetail findByMenuDetailName(String menuDetailName);
}
