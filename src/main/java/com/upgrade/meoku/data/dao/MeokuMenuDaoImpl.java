package com.upgrade.meoku.data.dao;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.dto.MeokuDetailedMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public MeokuMenuDetail searchMenuDetailAndSave(String menuName){
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

    //startDate ~  endDate 입력받고 해당 주간 DailyMenu가져오기
    public List<MeokuDailyMenuDTO> searchDailyMenuOfWeekDays(Timestamp startDate, Timestamp endDate){
        List<MeokuDailyMenu> dailyMenuList = dailyMenuRepository.findByDateBetween(startDate, endDate);

        List<MeokuDailyMenuDTO> dailyMenuDTOList = new ArrayList<>();
        //Entity to DTO
        for(MeokuDailyMenu dailyMenuEntity : dailyMenuList){
            MeokuDailyMenuDTO dailyMenuDTO = new MeokuDailyMenuDTO();

            dailyMenuDTO.setDate(dailyMenuEntity.getDate());
            dailyMenuDTO.setHolidayFg(dailyMenuEntity.getHolidayFg());
            dailyMenuDTO.setRestaurantOpenFg(dailyMenuEntity.getRestaurantOpenFg());

            //dailyMenu 아래 DetailedMenu Entity List to DTO List
            List<MeokuDetailedMenu> detailedMenuEntityList = dailyMenuEntity.getDetailedMenuList();

            List<MeokuDetailedMenuDTO> detailedMenuDTOList = new ArrayList<>();
            for(MeokuDetailedMenu detailedMenu : detailedMenuEntityList){
                MeokuDetailedMenuDTO detailedMenuDTO = new MeokuDetailedMenuDTO();

                detailedMenuDTO.setMainMenuYn(detailedMenu.getMainMenuYn());
                detailedMenuDTO.setDetailedMenuName(detailedMenu.getDetailedMenuName());

                //Menu이름 추출
                String mainMenuName = detailedMenu.getMainMenu() != null ? detailedMenu.getMainMenu().getMenuDetailName() : "N/A";
                String menu1Name = detailedMenu.getMenu1() != null ? detailedMenu.getMenu1().getMenuDetailName() : "N/A";
                String menu2Name = detailedMenu.getMenu2() != null ? detailedMenu.getMenu2().getMenuDetailName() : "N/A";
                String menu3Name = detailedMenu.getMenu3() != null ? detailedMenu.getMenu3().getMenuDetailName() : "N/A";
                String menu4Name = detailedMenu.getMenu4() != null ? detailedMenu.getMenu4().getMenuDetailName() : "N/A";
                String menu5Name = detailedMenu.getMenu5() != null ? detailedMenu.getMenu5().getMenuDetailName() : "N/A";
                String menu6Name = detailedMenu.getMenu6() != null ? detailedMenu.getMenu6().getMenuDetailName() : "N/A";

                detailedMenuDTO.setMainMenuName(mainMenuName);
                detailedMenuDTO.setMenu1Name(menu1Name);
                detailedMenuDTO.setMenu2Name(menu2Name);
                detailedMenuDTO.setMenu3Name(menu3Name);
                detailedMenuDTO.setMenu4Name(menu4Name);
                detailedMenuDTO.setMenu5Name(menu5Name);
                detailedMenuDTO.setMenu6Name(menu6Name);

                detailedMenuDTOList.add(detailedMenuDTO);
            }
            //식단 상세목록 날별메뉴 DTO에 넣기
            dailyMenuDTO.setDetailedMenuDTOList(detailedMenuDTOList);

            dailyMenuDTOList.add(dailyMenuDTO);
        }

        return dailyMenuDTOList;
    }

}