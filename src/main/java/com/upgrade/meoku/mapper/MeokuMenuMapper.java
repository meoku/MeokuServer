package com.upgrade.meoku.mapper;

import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.dto.MeokuDetailedMenuDTO;
import com.upgrade.meoku.data.dto.MeokuMenuDetailDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeokuMenuMapper {
    MeokuMenuMapper MEOKU_MENU_MAPPER_INSTANCE = Mappers.getMapper(MeokuMenuMapper.class);

    MeokuDailyMenuDTO DailyMenuToDTO(MeokuDailyMenu meokuDailyMenu);
    MeokuDailyMenu DailyMenuDTOToEntity(MeokuDailyMenuDTO meokuDailyMenuDTO);

    MeokuDetailedMenuDTO detailedMenuToDTO(MeokuDetailedMenu meokuDetailedMenu);
    MeokuDetailedMenu detailedMenuDTOToEntity(MeokuDetailedMenuDTO meokuDetailedMenuDTO);

    MeokuMenuDetailDTO menuDetailDTOToEntity(MeokuMenuDetailDTO meokuMenuDetailDTO);
    MeokuMenuDetail menuDetailToDTO(MeokuMenuDetail meokuMenuDetail);
}
