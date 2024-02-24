package com.upgrade.meoku.data.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MeokuMenuDetailDTO {

    private Integer menuDetailId;
    private Integer detailedMenuId;
    private String menuDetailName;
    private Integer frequencyCnt;
    private Integer likesCnt;
    private Integer recentDetailedMenuId;
    private String recentDetailedMenuUrl;
    private Integer calories;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    private List<MeokuMenuTagsDTO> menuTagsDTOList;
    private List<MeokuMenuAllergiesDTO> menuAllergiesDTOList;
}

