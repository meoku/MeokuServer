package com.upgrade.meoku.newmealmenu.dao;

import com.upgrade.meoku.mealmenu.data.dao.SubMenuDao;
import com.upgrade.meoku.mealmenu.data.dto.*;
import com.upgrade.meoku.mealmenu.data.entity.*;
import com.upgrade.meoku.mealmenu.data.mapper.MenuMapper;
import com.upgrade.meoku.mealmenu.data.repository.*;
import com.upgrade.meoku.mealmenu.util.MenuUtil;
import com.upgrade.meoku.util.MeokuUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.upgrade.meoku.mealmenu.data.mapper.MenuMapper.MENU_MAPPER_INSTANCE;

@SpringBootTest
public class newMealMenuDaoTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    SubDailyMenuRepository dailyMenuRepository;
    @Autowired
    SubMenuDetailsRepository detailsRepository;
    @Autowired
    SubMenuDetailsItemBridgeRepository bridgeRepository;
    @Autowired
    SubMenuItemRepository menuItemRepository;
    @Autowired
    SubMenuTagRepository menuTagRepository;
    @Autowired
    SubMenuDao menuDao;

    @Test
    @DisplayName("만료된 태그 삭제하기")
    public void deleteExpiredTagTest(){
        Timestamp now = MenuUtil.getCurrentTimestamp();
        Long deleteCnt = menuDao.deleteExpiredMenuTag(now);
        System.out.println(deleteCnt);
    }
    @Test
    @DisplayName("만료된 테그 가져오기")
    public void srchExpiredTagTest(){
        Timestamp now = MenuUtil.getCurrentTimestamp();
        List<SubMenuTagDTO> srchTags = menuDao.searchExpiredMenuTag(now);

        for(SubMenuTagDTO srchTag : srchTags){
            System.out.println(srchTag);
        }
    }
    @Test
    @DisplayName("메뉴태그 가져오기")
    public void searchMenuTag(){
        // menuItem id 준비
        List<Integer> menuIdList = new ArrayList<>();
        menuIdList.add(303);
        menuIdList.add(304);
        menuIdList.add(305);

        // 현재 날짜와 시간을 가져옴
        LocalDateTime now = LocalDateTime.now();
        // LocalDateTime을 Timestamp로 변환
        Timestamp curTimestamp = Timestamp.valueOf(now);


        List<SubMenuItemDTO> searchedMenuTagDTOList = menuDao.searchMenuTag(menuIdList);

        for(SubMenuItemDTO mtDTO : searchedMenuTagDTOList){
            System.out.println(mtDTO.toString());
        }
    }

    @Test
    @DisplayName("메뉴에 New 태그 붙이기")
    public void insertMenuItemAndTag() throws Exception {
        String menuName = "Test";

        SubMenuItem savedMenuItem = menuDao.menuItemCountUpAndSave(menuName);
        // 만약 메뉴 이름이 ""라서 null이 반환됐다면 bridge를 포함항 menuItem데이터 저장하지 않아야함
        if(savedMenuItem == null) throw new Exception();

        //새로운 메뉴라면 New 태그 저장 (횟수가 1번일때가 처음 저장된 메뉴)
        if(savedMenuItem.getFrequencyCnt() == 1){
            SubMenuTag newMenuTag = new SubMenuTag();
            newMenuTag.setSubMenuItem(savedMenuItem);
            newMenuTag.setMenuTagName("NEW");
            // 15일뒤 날짜 가져오기
            Timestamp tagEndDate = MenuUtil.getTimestampAfterNdays(LocalDate.now(), 15);
            newMenuTag.setTagEndDate(tagEndDate);
            SubMenuTag savedTag = menuDao.insertMenuTag(newMenuTag);
        }

        System.out.println(savedMenuItem.getSubMenuTagList().get(0).getMenuTagName());
        System.out.println(savedMenuItem.getSubMenuTagList().get(0).getTagEndDate());
    }

    @Test
    @DisplayName("특정 날짜 데이터 삭제")
    public void deleteMenudata(){
        // 원하는 날짜 입력 (예: 2024년 7월 28일)
        LocalDate localDate = LocalDate.of(2024, 8, 2);
        // LocalDate를 LocalDateTime으로 변환 (하루의 시작 시각으로 설정)
        LocalDateTime localDateTime = localDate.atStartOfDay();
        // LocalDateTime을 Timestamp로 변환
        Timestamp deleteDate = Timestamp.valueOf(localDateTime);

        SubDailyMenuDTO dailyMenuDtoBeforDeleting = menuDao.searchDailyMenuOfDay(deleteDate);
        System.out.println(dailyMenuDtoBeforDeleting);

        boolean deleteYn = menuDao.deleteMenuData(deleteDate);
        System.out.println(deleteYn);

        SubDailyMenuDTO DailyMenuDtoAfterDeleting = menuDao.searchDailyMenuOfDay(deleteDate);
        System.out.println(DailyMenuDtoAfterDeleting);
    }

    @Test
    @DisplayName("원하는 날짜만 데이터 가져오기")
    public void srchMenuDateOfDay(){
        // 원하는 날짜 입력 (예: 2024년 7월 28일)
        LocalDate localDate = LocalDate.of(2024, 8, 2);
        // LocalDate를 LocalDateTime으로 변환 (하루의 시작 시각으로 설정)
        LocalDateTime localDateTime = localDate.atStartOfDay();
        // LocalDateTime을 Timestamp로 변환
        Timestamp srchDate = Timestamp.valueOf(localDateTime);

        SubDailyMenuDTO srchDailyMenuDto = menuDao.searchDailyMenuOfDay(srchDate);

        System.out.println(srchDailyMenuDto.getMenuDate());
        for(SubMenuDetailsDTO mdd : srchDailyMenuDto.getMenuDetailsList()){
            System.out.println(mdd.getMenuDetailsName());
            for(SubMenuDetailsItemBridgeDTO mdibd : mdd.getSubBridgeList()){
                System.out.println(mdibd.getMenuItemName());
            }
            System.out.println(" ");
        }
    }

    @Test
    @DisplayName("주간 식단데이터 가져오기")
    public void searchDailyMenuOfWeekDays(){
        LocalDate date = LocalDate.of(2024, 07, 8);

        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(date);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));



        List<SubDailyMenuDTO> srchDm = menuDao.searchDailyMenuOfWeek(startTimestamp, endTimestamp);
        for(SubDailyMenuDTO dmd : srchDm){
            System.out.println(dmd.getMenuDate());
            for(SubMenuDetailsDTO mdd : dmd.getMenuDetailsList()){
                System.out.println(mdd.getMenuDetailsName() + ", Main Meal Yn : " + mdd.getMainMealYn());
                for(SubMenuDetailsItemBridgeDTO bd : mdd.getSubBridgeList()){
                    System.out.println(bd.getMenuItemName() + ", " + bd.getMainMenuYn());
                }
                System.out.println("");
            }
        }
    }

    @Test
    @Disabled
    @DisplayName("저장된 식단데이터 가져오기 - 파기")
    public void searchDailyMenuOfWeek(){
        LocalDate date = LocalDate.of(2024, 07, 8);

        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(date);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        //일일메뉴, 상세식단 데이터까지 불러오기 (Hibernate의 bag 관련 이슈로 한번에 3개 테이블 의 데이터 못가져온다고 함)
        String jpql = "SELECT dm FROM SubDailyMenu dm " +
                "LEFT JOIN FETCH dm.menuDetailsList details " +
                "LEFT JOIN FETCH details.subBridgeList " +
                "WHERE dm.menuDate BETWEEN :startDate AND :endDate";

        List<SubDailyMenu> dailyMenus =  entityManager.createQuery(jpql, SubDailyMenu.class)
                .setParameter("startDate", startTimestamp)
                .setParameter("endDate", endTimestamp)
                .getResultList();

        for(SubDailyMenu dm : dailyMenus) {
            for (SubMenuDetails details : dm.getMenuDetailsList()) {
                Hibernate.initialize(details.getSubBridgeList());
            }
        }

        for(SubDailyMenu dm : dailyMenus) {
            System.out.println(dm.getDailyMenuId());
            for(SubMenuDetails md : dm.getMenuDetailsList()){
                System.out.println(md.getDailyMenuDate());
                System.out.println(md.getSubBridgeList());
            }
        }
    }

    @Test
    @DisplayName("태그 DTO -> Entity 변환 후 저장")
    public void insertMenuTagTest(){
        String dateString = "2024-07-15";

        // 문자열을 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // 한 달 뒤의 날짜 계산
        LocalDate oneMonthLaterDate = localDate.plusMonths(1);

        // LocalDate를 Timestamp로 변환
        Timestamp oneMonthLaterTimestamp = Timestamp.valueOf(oneMonthLaterDate.atStartOfDay());



        SubMenuTagDTO savedMenuTagDTO = new SubMenuTagDTO();
        savedMenuTagDTO.setMenuItemId(32);
        savedMenuTagDTO.setMenuTagName("NEW");
        savedMenuTagDTO.setTagEndDate(oneMonthLaterTimestamp);

        System.out.println(savedMenuTagDTO);
        SubMenuTag savedSubMenuTag = MENU_MAPPER_INSTANCE.menuTagDtoToEntity(savedMenuTagDTO);
        System.out.println(savedSubMenuTag);
        menuTagRepository.save(savedSubMenuTag);
    }
}
