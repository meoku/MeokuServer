package com.upgrade.meoku.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeokuLoginRequestDto {

    @NotNull(message = "이메일은 필수입니다.")
    @Email
    private String email;

    @NotNull(message = "패스워드 필수입니다.")
    private String password;
}
