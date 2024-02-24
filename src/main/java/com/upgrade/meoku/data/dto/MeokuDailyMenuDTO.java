package com.upgrade.meoku.data.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class MeokuDailyMenuDTO {

    private Integer dailyMenuId;
    private Date date;
    private String holidayFg;
    private String restaurantOpenFg;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    private List<MeokuDetailedMenuDTO> detailedMenuDTOList;
    // Getters and setters
}
