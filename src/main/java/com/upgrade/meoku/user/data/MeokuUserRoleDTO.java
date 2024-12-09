package com.upgrade.meoku.user.data;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class MeokuUserRoleDTO {
    private Integer roleId;

    private Integer userId;
    private String roleName; // e.g., "ROLE_USER", "ROLE_ADMIN"

}
