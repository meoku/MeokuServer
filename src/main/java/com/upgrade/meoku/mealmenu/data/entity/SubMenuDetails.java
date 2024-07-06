package com.upgrade.meoku.mealmenu.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "SUB_MENU_DETAILS")
public class SubMenuDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_DETAILS_ID")
    private Integer menuDetailsId;

    @Column(name = "DAILY_MENU_ID")
    private Integer dailyMenuId;
    @Column(name = "DAILY_MENU_DATE")
    private Timestamp dailyMenuDate;
    @Column(name = "DAILY_MENU_CATEGORY")
    private String dailyMenuCategory;
    @Column(name = "MAIN_MENU_YN")
    private String mainMenuYn;
    @Column(name = "MENU_DETAILS_NAME")
    private String menuDetailsName;
    @Column(name = "MENU_DETAILS_IMG_URL")
    private String menuDetailsImgUrl;

    @OneToMany(mappedBy = "subMenuDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubMenuDetailsItemBridge> subBridgeList = new ArrayList<>();
}
