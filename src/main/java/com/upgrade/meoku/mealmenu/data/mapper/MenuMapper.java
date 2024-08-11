package com.upgrade.meoku.mealmenu.data.mapper;

import com.upgrade.meoku.mealmenu.data.dto.*;
import com.upgrade.meoku.mealmenu.data.entity.*;
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
    @Mapping(target = "subBridgeList", ignore = true) // bridge필드는 변경 제외
    SubMenuItemDTO menuItemEntityToDtoNoBridge(SubMenuItem menuItem);

    @Mapping(source = "menuItemId", target = "subMenuItem.menuItemId") // menuItemId를 subMenuItem의 id에 매핑
//    @Mapping(target = "subMenuItem.name", ignore = true) // 필요에 따라 다른 필드도 추가적으로 매핑 가능
    SubMenuTag menuTagDtoToEntity(SubMenuTagDTO menuTagDTO);
    SubMenuTagDTO menuTagEntityToDto(SubMenuTag menuTag);

//    SubMenuAllergies menuAllergiesDtoToEntity(SubMenuAllergiesDTO menuAllergiesDTO);
//    SubMenuAllergiesDTO menuAllergiesEntityToDto(SubMenuAllergies menuAllergies);

}
