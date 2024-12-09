package com.upgrade.meoku.user.data;

import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MeokuUserDTO {
    //PK
    private Integer userId;

    private String id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Integer age;
    private String sex;

    private List<MeokuUserRoleDTO> userRoleDTOList = new ArrayList<>();
}
