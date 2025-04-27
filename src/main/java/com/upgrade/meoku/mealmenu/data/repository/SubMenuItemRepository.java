package com.upgrade.meoku.mealmenu.data.repository;

import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface SubMenuItemRepository extends JpaRepository<SubMenuItem, Integer> {
    SubMenuItem findByMenuItemName(String menuDetailName);

    @Query("SELECT mi FROM SubMenuItem mi WHERE mi.menuItemName LIKE %:menuItemName%")
    List<SubMenuItem> findByMenuItemNameContaining(@Param("menuItemName") String menuItemName);

    Optional<SubMenuItem> findByMenuItemId(Integer menuItemId);
    // 메뉴id list를 받아 해당하는 모든 태그를 가져오기
//    @Query(value = "SELECT ITEM.*, TAG.MENU_TAG_NAME" +
//            "       FROM SUB_MENU_ITEM ITEM JOIN SUB_MENU_TAG TAG " +
//            "           ON ITEM.MENU_ITEM_ID = TAG.MENU_ITEM_ID  " +
//            "           AND ITEM.MENU_ITEM_ID IN :menuItemIds " +
//            "           AND TAG.TAG_END_DATE >= :currentDate", nativeQuery = true)

    @Query("SELECT mi FROM SubMenuItem mi LEFT JOIN FETCH mi.subMenuTagList t WHERE mi.menuItemId IN :menuItemIds AND t.tagEndDate >= :currentDate")
    List<SubMenuItem> findAllByMenuItemIdsAndTagEndDateAfter(
            @Param("menuItemIds") List<Integer> menuItemIds,
            @Param("currentDate") Timestamp currentDate
    );
}
