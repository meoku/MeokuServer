package com.upgrade.meoku.data.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class MeokuDetailedMenuDTO {

    private int detailedMenuId;
    private int dailyMenuId;
    private Date dailyMenuDate;
    private String dailyMenuCategory;
    private String mainMenuYn;
    private String detailedMenuName;
    private String detailedMenuImgUrl;
    private int mainMenuId;
    private String mainMenuName;
    private int menu1DetailId;
    private String menu1Name;
    private int menu2DetailId;
    private String menu2Name;
    private int menu3DetailId;
    private String menu3Name;
    private int menu4DetailId;
    private String menu4Name;
    private int menu5DetailId;
    private String menu5Name;
    private int menu6DetailId;
    private String menu6Name;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    private List<MeokuMenuDetailDTO> menuDetailDTOList;
    // Getters and setters
}
