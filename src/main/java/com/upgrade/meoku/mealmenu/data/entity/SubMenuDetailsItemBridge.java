package com.upgrade.meoku.mealmenu.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "SUB_MENU_DETAILS_ITEM_BRIDGE")
public class SubMenuDetailsItemBridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRIDGE_ID")
    private Integer bridgeId;

//    @Column(name = "MENU_DETAILS_ID")
//    private Integer menuDetailsId;
    @Column(name = "MENU_ITEM_NAME")
    private String menuItemName;
    @Column(name = "MAIN_MENU_YN")
    private String mainMenuYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_DETAILS_ID")
    private SubMenuDetails subMenuDetails;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_ITEM_ID")
    private SubMenuItem subMenuItem;
}
