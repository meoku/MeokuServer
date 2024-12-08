package com.upgrade.meoku.user;

import lombok.Data;

@Data
public class MeokuUserDTO {
    private Integer userId;
    private String id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Integer age;
    private String sex;
}
