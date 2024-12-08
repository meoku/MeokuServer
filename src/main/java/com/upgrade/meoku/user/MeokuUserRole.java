package com.upgrade.meoku.user;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "MEOKU_USER_ROLE")
public class MeokuUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Integer roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private MeokuUser user;

    @Column(name = "ROLE_NAME", nullable = false)
    private String roleName; // e.g., "ROLE_USER", "ROLE_ADMIN"

    @Column(name = "DESCRIPTION")
    private String description; // 권한 설명 (선택)

    @CreationTimestamp
    @Column(name = "CREATED_DATE", updatable = false)
    private Timestamp createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;

}
