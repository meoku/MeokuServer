package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
//메뉴 태그
@Entity
@Table(name = "MEOKU_MENU_TAGS")
public class MeokuMenuTags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_TAG_ID")
    private Integer menuTagId;
    @Column(name = "MENU_DETAIL_ID", nullable = false)
    private Integer menuDetailId;
    @Column(name = "MENU_TAG_NAME")
    private String menuTagName;
    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
