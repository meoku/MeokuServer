package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface SubMenuTagRepository extends JpaRepository<SubMenuTag, Integer> {
    List<SubMenuTag> findBySubMenuItem(SubMenuItem subMenuItem);

}
