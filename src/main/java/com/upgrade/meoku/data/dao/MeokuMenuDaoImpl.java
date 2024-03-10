package com.upgrade.meoku.data.dao;

import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeokuMenuDaoImpl implements MeokuMenuDao{

    private MeokuDailyMenuRepository dailyMenuRepository;
    private MeokuDetailedMenuRepository detailedMenuRepository;
    private MeokuMenuDetailRepository menuDetailRepository;
    private MeokuMenuTagsRepository menuTagsRepository;
    private MeokuMenuAllergiesRepository menuAllergiesRepository;

    @Autowired
    public MeokuMenuDaoImpl(MeokuDailyMenuRepository dailyMenuRepository,
                            MeokuDetailedMenuRepository detailedMenuRepository,
                            MeokuMenuDetailRepository menuDetailRepository,
                            MeokuMenuTagsRepository menuTagsRepository,
                            MeokuMenuAllergiesRepository menuAllergiesRepository){
        this.dailyMenuRepository = dailyMenuRepository;
        this.detailedMenuRepository = detailedMenuRepository;
        this.menuDetailRepository = menuDetailRepository;
        this.menuTagsRepository = menuTagsRepository;
        this.menuAllergiesRepository = menuAllergiesRepository;
    }

    //Daily Menu 저장
    public void insertDailyMenu(MeokuDailyMenu dailyMenu){
        dailyMenuRepository.save(dailyMenu);
    }
    //Detailed Menu 저장
    public void insertDetailedMenu(List<MeokuDetailedMenu> detailedMenuList){
        detailedMenuRepository.saveAll(detailedMenuList);
    }
    //Menu Detail 저장
    public void inserMenuDetail(MeokuMenuDetail meokuMenuDetail){
        menuDetailRepository.save(meokuMenuDetail);
    }
    public void insertMenuDetailList(List<MeokuMenuDetail> menuDetailList){
        menuDetailRepository.saveAll(menuDetailList);
    }

    //Menu 이름대로 MenuDetail에서 찾기
    public MeokuMenuDetail searchMenuDetail(String menuName){
        //메뉴이름 이 없으면 null 반환
        if(menuName == null || menuName.equals("")) return null;

        MeokuMenuDetail searchedMenuDetail = menuDetailRepository.findByMenuDetailName(menuName);
        if (searchedMenuDetail == null) {
            // 데이터가 없으면 새로운 데이터 생성
            searchedMenuDetail = new MeokuMenuDetail();
            searchedMenuDetail.setMenuDetailName(menuName);
            searchedMenuDetail.setFrequencyCnt(1); // 초기 값은 1로 설정
        } else {
            // 데이터가 존재하면 frequencyCnt 증가
            searchedMenuDetail.setFrequencyCnt(searchedMenuDetail.getFrequencyCnt() + 1);
        }
        return menuDetailRepository.save(searchedMenuDetail);
    }


}

