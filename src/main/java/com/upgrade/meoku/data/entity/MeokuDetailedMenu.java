package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

//상세 식단
@Data
@Entity
@Table(name = "MEOKU_DETAILED_MENU")
public class MeokuDetailedMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAILED_MENU_ID")
    private Integer detailedMenuId;
//    @Column(name = "DAILY_MENU_ID", nullable = false)//불필요
//    private Integer dailyMenuId;
    //MEOKU_DAILY_MENU와 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAILY_MENU_ID")
    private MeokuDailyMenu meokuDailyMenu;
    @Column(name = "DAILY_MENU_DATE")
    private Timestamp dailyMenuDate;
    @Column(name = "DAILY_MENU_CATEGORY")
    private String dailyMenuCategory;
    @Column(name = "MAIN_MENU_YN")
    private String mainMenuYn;
    @Column(name = "DETAILED_MENU_NAME")
    private String detailedMenuName;
    @Column(name = "DETAILED_MENU_IMG_URL")
    private String detailedMenuImgUrl;

//    @Column(name = "MAIN_MENU_ID")
//    private Integer mainMenuId;
//    @Column(name = "MAIN_MENU_NAME")
//    private String mainMenuName;
//
//    @Column(name = "MENU1_DETAIL_ID")
//    private Integer menu1DetailId;
//    @Column(name = "MENU1_NMAE")
//    private String menu1Name;
//
//    @Column(name = "MENU2_DETAIL_ID")
//    private Integer menu2DetailId;
//    @Column(name = "MENU2_NMAE")
//    private String menu2Name;
//
//    @Column(name = "MENU3_DETAIL_ID")
//    private Integer menu3DetailId;
//    @Column(name = "MENU3_NMAE")
//    private String menu3Name;
//
//    @Column(name = "MENU4_DETAIL_ID")
//    private Integer menu4DetailId;
//    @Column(name = "MENU4_NMAE")
//    private String menu4Name;
//
//    @Column(name = "MENU5_DETAIL_ID")
//    private Integer menu5DetailId;
//    @Column(name = "MENU5_NMAE")
//    private String menu5Name;
//
//    @Column(name = "MENU6_DETAIL_ID")
//    private Integer menu6DetailId;
//    @Column(name = "MENU6_NMAE")
//    private String menu6Name;

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

    //Menu Detail 정보
    @ManyToOne
    @JoinColumn(name = "MAIN_MENU_ID")
    private MeokuMenuDetail mainMenu;
    @ManyToOne
    @JoinColumn(name = "MENU1_DETAIL_ID")
    private MeokuMenuDetail menu1;
    @ManyToOne
    @JoinColumn(name = "MENU2_DETAIL_ID")
    private MeokuMenuDetail menu2;
    @ManyToOne
    @JoinColumn(name = "MENU3_DETAIL_ID")
    private MeokuMenuDetail menu3;
    @ManyToOne
    @JoinColumn(name = "MENU4_DETAIL_ID")
    private MeokuMenuDetail menu4;
    @ManyToOne
    @JoinColumn(name = "MENU5_DETAIL_ID")
    private MeokuMenuDetail menu5;
    @ManyToOne
    @JoinColumn(name = "MENU6_DETAIL_ID")
    private MeokuMenuDetail menu6;

}

