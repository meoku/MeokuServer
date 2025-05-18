package com.upgrade.meoku.user.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeokuLoginRequestDTO {

    @NotNull(message = "id는 필수입니다.")
    private String id;

    @NotNull(message = "패스워드 필수입니다.")
    private String password;
}
