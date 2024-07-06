package com.upgrade.meoku.mealmenu.data.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubMenuDetailsDTO {
    private Integer menuDetailsId;

    private Integer dailyMenuId;
    private Timestamp dailyMenuDate;
    private String dailyMenuCategory;
    private String mainMealYn;
    private String menuDetailsName;
    private String menuDetailsImgUrl;

    private List<SubMenuDetailsItemBridgeDTO> subBridgeList = new ArrayList<>();
}
