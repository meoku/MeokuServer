package com.upgrade.meoku.mealmenu.data.dao;


import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetailsItemBridge;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import com.upgrade.meoku.mealmenu.data.repository.SubDailyMenuRepository;
import com.upgrade.meoku.mealmenu.data.repository.SubMenuDetailsItemBridgeRepository;
import com.upgrade.meoku.mealmenu.data.repository.SubMenuDetailsRepository;
import com.upgrade.meoku.mealmenu.data.repository.SubMenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class SubMenuDaoImpl implements SubMenuDao{

    private final SubDailyMenuRepository dailyMenuRepository;
    private final SubMenuDetailsRepository menuDetailsRepository;
    private final SubMenuItemRepository menuItemRepository;
    private final SubMenuDetailsItemBridgeRepository bridgeRepository;
    @Autowired
    public SubMenuDaoImpl(SubDailyMenuRepository dailyMenuRepository,
                          SubMenuDetailsRepository menuDetailsRepository,
                          SubMenuItemRepository menuItemRepository,
                          SubMenuDetailsItemBridgeRepository bridgeRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.menuDetailsRepository = menuDetailsRepository;
        this.menuItemRepository = menuItemRepository;
        this.bridgeRepository = bridgeRepository;
    }

    @Override
    public SubDailyMenu insertDailyMenu(SubDailyMenu dailyMenu) {
        return dailyMenuRepository.save(dailyMenu);
    }

    @Override
    public SubMenuDetails insertMenuDetails(SubMenuDetails menuDetails) {
        return menuDetailsRepository.save(menuDetails);
    }

    @Override
    public void insertDetailedMenu(List<MeokuDetailedMenu> detailedMenuList) {

    }

    @Override
    public void inserMenuDetail(MeokuMenuDetail meokuMenuDetail) {

    }

    @Override
    public void insertBridgeList(List<SubMenuDetailsItemBridge> bridgeList) {
        bridgeRepository.saveAll(bridgeList);
    }

    @Override
    public void insertMenuDetailList(List<SubMenuDetails> menuDetailsList) {
    }

    @Override
    public SubMenuItem menuItemCountUpAndSave(String menuName) {
        //메뉴이름 이 없으면 null 반환
        if(menuName == null || menuName.equals("")) return null;

        SubMenuItem searchedMenuItem = menuItemRepository.findByMenuItemName(menuName);
        if (searchedMenuItem == null) {
            // 데이터가 없으면 새로운 데이터 생성
            searchedMenuItem = new SubMenuItem();
            searchedMenuItem.setMenuItemName(menuName);
            searchedMenuItem.setFrequencyCnt(1); // 초기 값은 1로 설정
        } else {
            // 데이터가 존재하면 frequencyCnt 증가
            searchedMenuItem.setFrequencyCnt(searchedMenuItem.getFrequencyCnt() + 1);
        }
        return menuItemRepository.save(searchedMenuItem);
    }

    @Override
    public SubDailyMenu searchDailyMenu(Timestamp searchDate) {
        Optional<SubDailyMenu> optDailyMenu = dailyMenuRepository.findByMenuDate(searchDate);

        //없으면 null
        if(!optDailyMenu.isPresent()) return null;

        return optDailyMenu.get();
    }

    @Override
    public List<SubDailyMenu> searchDailyMenuOfWeekDays(Timestamp startDate, Timestamp endDate) {
        return null;
    }
}
