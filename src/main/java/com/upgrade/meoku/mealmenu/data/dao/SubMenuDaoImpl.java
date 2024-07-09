package com.upgrade.meoku.mealmenu.data.dao;


import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetailsItemBridge;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import com.upgrade.meoku.mealmenu.data.repository.SubDailyMenuRepository;
import com.upgrade.meoku.mealmenu.data.repository.SubMenuDetailsItemBridgeRepository;
import com.upgrade.meoku.mealmenu.data.repository.SubMenuDetailsRepository;
import com.upgrade.meoku.mealmenu.data.repository.SubMenuItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.upgrade.meoku.mealmenu.data.mapper.MenuMapper.MENU_MAPPER_INSTANCE;

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

    //주간 메뉴데이터 가져오기
    @Override
    public List<SubDailyMenuDTO> searchDailyMenuOfWeek(Timestamp startDate, Timestamp endDate) {
        //일일 데이터 가져오기
        List<SubDailyMenu> srchDailyMenuList = dailyMenuRepository.findByMenuDateBetween(startDate, endDate);

        //상세 식단 가져오기
        for(SubDailyMenu dm : srchDailyMenuList){
            List<SubMenuDetails> srchMenuDetailsList = menuDetailsRepository.findBySubDailyMenu(dm);
            dm.setMenuDetailsList(srchMenuDetailsList);
            //Bridge 가져오기(반 정규화로 Bridge에 메뉴이름, 메인 여부 담아놓음)
            for(SubMenuDetails md : dm.getMenuDetailsList()){
                List<SubMenuDetailsItemBridge> srchBridgeList = bridgeRepository.findBySubMenuDetails(md);
                md.setSubBridgeList(srchBridgeList);
            }
        }
        // mapStruct를 이용해 Entity -> DTO로 변환
        List<SubDailyMenuDTO> srchDailyMenuDTOList = new ArrayList<>();
        for(SubDailyMenu srchDailyMenu : srchDailyMenuList){
            srchDailyMenuDTOList.add(MENU_MAPPER_INSTANCE.dailyMenuEntityToDto(srchDailyMenu));
        }

        return srchDailyMenuDTOList;
    }
}
