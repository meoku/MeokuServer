package com.upgrade.meoku.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MeokuDailyMenuDTO {

    private Integer dailyMenuId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp menuDate;
    private String holidayFg;
    private String restaurantOpenFg;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    private List<MeokuDetailedMenuDTO> detailedMenuList;
}
