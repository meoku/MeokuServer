package com.upgrade.meoku.mealmenu.data.mapper;

import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsItemBridgeDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuItemDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetailsItemBridge;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;
import jakarta.persistence.Column;
import org.hibernate.annotations.UpdateTimestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

@Mapper
public interface MenuMapper {
    MenuMapper MENU_MAPPER_INSTANCE = Mappers.getMapper(MenuMapper.class);

    SubDailyMenu dailyMenuDtoToEntity(SubDailyMenuDTO dailyMenuDTO);
    SubDailyMenuDTO dailyMenuEntityToDto(SubDailyMenu dailyMenu);
    SubMenuDetails menuDetailsDtoToEntity(SubMenuDetailsDTO menuDetailsDTO);
    SubMenuDetailsDTO menuDetailsEntityToDto(SubMenuDetails menuDetails);

    SubMenuDetailsItemBridge bridgeDtoToEntity(SubMenuDetailsItemBridgeDTO bridgeDTO);
    SubMenuDetailsItemBridgeDTO bridgeEntityToDto(SubMenuDetailsItemBridge bridge);

    SubMenuItem menuItemDtoToEntity(SubMenuItemDTO menuItemDTO);
    SubMenuItemDTO menuItemEntityToDto(SubMenuItem menuItem);
}
