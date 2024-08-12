package com.upgrade.meoku.mealmenu.data.dao;

import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuItemDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuTagDTO;
import com.upgrade.meoku.mealmenu.data.entity.*;
import com.upgrade.meoku.mealmenu.data.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final SubMenuTagRepository menuTagRepository;
    private final SubMenuItemRepository subMenuItemRepository;

    @Autowired
    public SubMenuDaoImpl(SubDailyMenuRepository dailyMenuRepository,
                          SubMenuDetailsRepository menuDetailsRepository,
                          SubMenuItemRepository menuItemRepository,
                          SubMenuDetailsItemBridgeRepository bridgeRepository,
                          SubMenuTagRepository menuTagRepository, SubMenuItemRepository subMenuItemRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.menuDetailsRepository = menuDetailsRepository;
        this.menuItemRepository = menuItemRepository;
        this.bridgeRepository = bridgeRepository;
        this.menuTagRepository = menuTagRepository;
        this.subMenuItemRepository = subMenuItemRepository;
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

    // 메뉴를 찾고 있으면 cnt + 1
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
                // tag도 가져오기 item entity는 생략

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

    // 특정 날짜 메뉴데이터 가져오기 (아직 필요없어서 제작 x )
    @Override
    public SubDailyMenuDTO searchDailyMenuOfDay(Timestamp searchDate){
        Optional<SubDailyMenu> srchDailyMenuOpt = dailyMenuRepository.findByMenuDate(searchDate);

        // 해당날짜에 데이터가 없을떄 null
        if(!srchDailyMenuOpt.isPresent()) return null;

        //daily 가져오기
        SubDailyMenu srchDailyMenu = srchDailyMenuOpt.get();
        //details 가져오기
        List<SubMenuDetails> deleteMenuDetailsList = menuDetailsRepository.findBySubDailyMenu(srchDailyMenu);
        srchDailyMenu.setMenuDetailsList(deleteMenuDetailsList);
        //Bridge 가져오기
        for(SubMenuDetails md : srchDailyMenu.getMenuDetailsList()){
            List<SubMenuDetailsItemBridge> srchBridgeList = bridgeRepository.findBySubMenuDetails(md);
            md.setSubBridgeList(srchBridgeList);
        }
        SubDailyMenuDTO srchDailyMenuDto = MENU_MAPPER_INSTANCE.dailyMenuEntityToDto(srchDailyMenu);

        return srchDailyMenuDto;
    }

    // 특정 날짜 메뉴데이터 삭제하기
    /*
    * 아래 순서로 삭제 진행
    * 1. daily 삭제 (orphanRemoval = true 조건으로 details, bridge까지 자동 삭제)
    * 2. item 삭제 or cnt - 1 (tag는 따로 삭제 안해도 orphanRemoval = true 조건으로 자동 삭제됨)
    * */
    @Override
    @Transactional
    public boolean deleteMenuData(Timestamp deleteDate) {
        boolean resultCode = false;

        Optional<SubDailyMenu> deletedDailyMenuOpt = dailyMenuRepository.findByMenuDate(deleteDate);
        // 해당날짜에 데이터가 없을떄 null
        if(!deletedDailyMenuOpt.isPresent()) return resultCode;

        //daily 가져오기
        SubDailyMenu deletedDailyMenu = deletedDailyMenuOpt.get();
        //details 가져오기
        List<SubMenuDetails> deleteMenuDetailsList = menuDetailsRepository.findBySubDailyMenu(deletedDailyMenu);

        // bridge를 모두 돌며
        //item은 cnt가 1인 경우만 삭제하고 2 이상일 경우는 cnt - 1
        for(SubMenuDetails md : deleteMenuDetailsList){
            //Bridge 가져오기
            List<SubMenuDetailsItemBridge> srchBridgeList = bridgeRepository.findBySubMenuDetails(md);
            for(SubMenuDetailsItemBridge b : srchBridgeList){
                Optional<SubMenuItem> srchMenuItemOpt = menuItemRepository.findByMenuItemId(b.getSubMenuItem().getMenuItemId());
                //만약 item 이 안찾아지면 null반환 (debug가 어려울 꺼라 수정 필요)
                if(!srchMenuItemOpt.isPresent()){ return resultCode; }
                SubMenuItem srchMenuItem = srchMenuItemOpt.get();

                // cnt 2 이상이면 -1
                if(srchMenuItem.getFrequencyCnt() > 1){
                    srchMenuItem.setFrequencyCnt(srchMenuItem.getFrequencyCnt() - 1);
                    menuItemRepository.save(srchMenuItem); // 변경사항 반영
                }
                // cnt 1이면 삭제
                else{
                    menuItemRepository.delete(srchMenuItem);
                }
            }
        }

        // daily만 지우면 orphanRemoval = true로 인해 해당 옵션이 있는 부모의 자식까지 삭제되므로 daily -> detail -> bridge 까지 삭제됨
        dailyMenuRepository.delete(deletedDailyMenu);

        resultCode = true;
        return resultCode;
    }

    //메뉴 이름으로 MenuItem Entity 찾기 (폐기 - 변경된 버전은 Item을 가져올떄 자동으로 tag를 가져오지 않음)
    @Override
    public SubMenuItem searchMenuItem(String menuName) {
        return subMenuItemRepository.findByMenuItemName(menuName);
    }

    //메뉴에 태그 저장
    @Override
    public SubMenuTag insertMenuTag(SubMenuTag subMenuTag) {
        return menuTagRepository.save(subMenuTag);
    }
    //메뉴 태그 가져오기
    @Override
    public List<SubMenuItemDTO> searchMenuTag(List<Integer> menuIdList) {
        // 현재 날짜와 시간을 가져옴
        LocalDateTime now = LocalDateTime.now();
        // LocalDateTime을 Timestamp로 변환
        Timestamp curTimestamp = Timestamp.valueOf(now);
        List<SubMenuItem> searchedTagList = menuItemRepository.findAllByMenuItemIdsAndTagEndDateAfter(menuIdList, curTimestamp);

        List<SubMenuItemDTO> searchedItemDTOList = new ArrayList<>();

        for(SubMenuItem mt : searchedTagList){
            SubMenuItemDTO mtDTO = MENU_MAPPER_INSTANCE.menuItemEntityToDtoNoBridge(mt);
            searchedItemDTOList.add(mtDTO);
        }

        return searchedItemDTOList;
    }
}
