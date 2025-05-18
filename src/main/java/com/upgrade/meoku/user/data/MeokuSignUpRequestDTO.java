package com.upgrade.meoku.user.data;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MeokuSignUpRequestDTO {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "아이디는 5~20자의 영문 또는 숫자여야 합니다.")
    String id;

//    @NotBlank(message = "이메일은 필수입니다.")
//    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
    String password;

    String name;

    String nickname;

    @NotBlank(message = "성별은 필수입니다.")
    @Pattern(regexp = "^(M|F)$", message = "잘못된 성별 표기입니다.")
    String sex;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150 이하여야 합니다.")
    int age;
}
