package com.upgrade.meoku.user;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "MEOKU_USER")
public class MeokuUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "ID", nullable = false, unique = true)
    private String id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "NAME")
    private String name;
    @Column(name = "NICKNAME")
    private String nickname;
    @Column(name = "AGE")
    private int age;
    @Column(name = "SEX")
    private String sex;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<MeokuUserRole> roles = new HashSet<>();

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
