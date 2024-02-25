package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
//상세식단
@Entity
@Table(name = "MEOKU_MENU_DETAIL")
public class MeokuMenuDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_DETAIL_ID")
    private Integer menuDetailId;

    @Column(name = "MENU_DETAIL_NAME")
    private String menuDetailName;
    @Column(name = "FREQUENCY_CNT")
    private Integer frequencyCnt;
    @Column(name = "LIKES_CNT")
    private Integer likesCnt;
    @Column(name = "RECENT_DETAILED_MENU_ID")
    private Integer recentDetailedMenuId;
    @Column(name = "RECENT_DETAILED_MENU_URL")
    private String recentDetailedMenuUrl;
    @Column(name = "CALORIES")
    private Integer calories;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;

}

