package com.upgrade.meoku.data.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class MeokuDetailedMenuDTO {

    private Integer detailedMenuId;
    private Integer dailyMenuId;
    private Date dailyMenuDate;
    private String dailyMenuCategory;
    private String mainMenu;
    private String detailedMenuName;
    private String detailedMenuImgUrl;
    private String mainMenuId;
    private Integer menu1DetailId;
    private Integer menu2DetailId;
    private Integer menu3DetailId;
    private Integer menu4DetailId;
    private Integer menu5DetailId;
    private Integer menu6DetailId;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    private List<MeokuMenuDetailDTO> menuDetailDTOList;
    // Getters and setters
}
