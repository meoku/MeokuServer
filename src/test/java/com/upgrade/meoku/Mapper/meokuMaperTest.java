package com.upgrade.meoku.Mapper;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.dto.MeokuDetailedMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.repository.MeokuDailyMenuRepository;
import com.upgrade.meoku.util.MeokuUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.upgrade.meoku.mapper.MeokuMenuMapper.MEOKU_MENU_MAPPER_INSTANCE;

@SpringBootTest
public class meokuMaperTest {

    @Autowired
    MeokuDailyMenuRepository meokuDailyMenuRepository;

    @Test
    @DisplayName("일 메뉴 데이타 Entity, DTO 변환 테스트")
    public void convertDailyMenu(){
        LocalDate localDate = LocalDate.of(2024,03,26);
        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(localDate);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        List<MeokuDailyMenu> dailyMenuList = meokuDailyMenuRepository.findByMenuDateBetween(startTimestamp, endTimestamp);

        for(MeokuDailyMenu meokuDailyMenu : dailyMenuList){
//            System.out.println(meokuDailyMenu.getDetailedMenuList().get(0).getMainMenu());

            MeokuDailyMenuDTO meokuDailyMenuDTO = MEOKU_MENU_MAPPER_INSTANCE.DailyMenuToDTO(meokuDailyMenu);
            System.out.println(meokuDailyMenuDTO.getDailyMenuId());
            System.out.println(meokuDailyMenuDTO.getMenuDate());
            System.out.println(meokuDailyMenuDTO.getHolidayFg());
            System.out.println(meokuDailyMenuDTO.getHolidayFg());
            System.out.println(meokuDailyMenuDTO.getRestaurantOpenFg());

            List<MeokuDetailedMenuDTO> menuDTOS = meokuDailyMenuDTO.getDetailedMenuList();

            for(MeokuDetailedMenuDTO menuDTO : menuDTOS){
                System.out.println(menuDTO.getDailyMenuCategory());
                System.out.println(menuDTO.getDailyMenuDate());
                System.out.println(menuDTO.getMainMenuYn());
                System.out.println(menuDTO.getMainMenuName());
                System.out.println(menuDTO.getMenu1Name());
                System.out.println(menuDTO.getMenu2Name());
                System.out.println(menuDTO.getMenu3Name());
                System.out.println(menuDTO.getMenu4Name());
                System.out.println(menuDTO.getMenu5Name());
                System.out.println(menuDTO.getMenu6Name());
            }


        }
    }
}
