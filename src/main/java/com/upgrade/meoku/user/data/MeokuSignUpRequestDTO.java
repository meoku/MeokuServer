package com.upgrade.meoku.user.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MeokuSignUpRequestDTO {
    @NotBlank(message = "아이디는 필수입니다.")
    String id;
    //@Email(message = "올바른 이메일 형식이어야 합니다.")
    String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    String password;
    String name;
    String nickname;
    String sex;
    int age;
}
