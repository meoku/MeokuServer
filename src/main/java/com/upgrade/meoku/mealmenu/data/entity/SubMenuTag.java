package com.upgrade.meoku.mealmenu.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.sql.Timestamp;

//메뉴 태그
@Data
@Entity
@Table(name = "SUB_MENU_TAG")
public class SubMenuTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_TAG_ID")
    private Integer menuTagId;
    @ManyToOne
    @JoinColumn(name = "MENU_ITEM_ID")
    private SubMenuItem subMenuItem;
    @Column(name = "MENU_TAG_NAME")
    private String menuTagName;
    @Column(name = "TAG_END_DATE")
    private Timestamp tagEndDate;

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
