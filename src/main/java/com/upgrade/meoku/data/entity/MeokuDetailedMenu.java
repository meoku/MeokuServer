package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Timestamp;

//상세 식단
@Entity
@Table(name = "MEOKU_DETAILED_MENU")
public class MeokuDetailedMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAILED_MENU_ID")
    private Integer detailedMenuId;

    @Column(name = "DAILY_MENU_ID", nullable = false)
    private Integer dailyMenuId;
    @Column(name = "DAILY_MENU_DATE")
    private Date dailyMenuDate;
    @Column(name = "DAILY_MENU_CATEGORY")
    private String dailyMenuCategory;
    @Column(name = "MAIN_MENU_YN")
    private String mainMenu;
    @Column(name = "DETAILED_MENU_NAME")
    private String detailedMenuName;
    @Column(name = "DETAILED_MENU_IMG_URL")
    private String detailedMenuImgUrl;
    @Column(name = "MAIN_MENU_ID")
    private String mainMenuId;
    @Column(name = "MENU1_DETAIL_ID")
    private Integer menu1DetailId;
    @Column(name = "MENU2_DETAIL_ID")
    private Integer menu2DetailId;
    @Column(name = "MENU3_DETAIL_ID")
    private Integer menu3DetailId;
    @Column(name = "MENU4_DETAIL_ID")
    private Integer menu4DetailId;
    @Column(name = "MENU5_DETAIL_ID")
    private Integer menu5DetailId;
    @Column(name = "MENU6_DETAIL_ID")
    private Integer menu6DetailId;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;

}

