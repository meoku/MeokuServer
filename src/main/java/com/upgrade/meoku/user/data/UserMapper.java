package com.upgrade.meoku.user.data;

import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubDailyMenu;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper USER_MAPPER_INSTANCE = Mappers.getMapper(UserMapper.class);


    MeokuUser userDtoToEntity(MeokuUserDTO userDTO);
    @Mapping(source = "userRoleList", target = "userRoleDTOList")
    MeokuUserDTO userEntityToDto(MeokuUser user);
    MeokuUserRole userRoleDtoToEntity(MeokuUserRoleDTO userRoleDTO);
    MeokuUserRoleDTO userRoleEntityToDto(MeokuUserRole userRole);

}
