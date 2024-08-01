package com.upgrade.meoku.mealmenu.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "SUB_MENU_ITEM")
public class SubMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ITEM_ID")
    private Integer menuItemId;

    @Column(name = "RECENT_MENU_DETAILS_ID")
    private Integer recentMenuDetailsId;
    @Column(name = "RECENT_MENU_DETAILS_URL")
    private String recentMenuDetailsUrl;
    @Column(name = "MAIN_MENU_YN")
    private String mainMenuYn;
    @Column(name = "MENU_ITEM_NAME")
    private String menuItemName;
    @Column(name = "FREQUENCY_CNT")
    private Integer frequencyCnt;
    @Column(name = "LIKES_CNT")
    private Integer likesCnt;
    @Column(name = "CALORIES")
    private Integer calories;

    @OneToMany(mappedBy = "subMenuItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubMenuDetailsItemBridge> subBridgeList = new ArrayList<>();
    @OneToMany(mappedBy = "subMenuItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SubMenuTag> subMenuTagList = new ArrayList<>();
}
