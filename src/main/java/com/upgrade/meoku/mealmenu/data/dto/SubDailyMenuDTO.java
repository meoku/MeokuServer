package com.upgrade.meoku.mealmenu.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upgrade.meoku.data.dto.MeokuDetailedMenuDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubDailyMenuDTO {
    private Integer dailyMenuId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp menuDate;
    private String holidayFg;
    private String restaurantOpenFg;
//    private Timestamp createdDate;
//    private String createdBy;
//    private Timestamp updatedDate;
//    private String updatedBy;

    private List<SubMenuDetailsDTO> menuDetailsList = new ArrayList<>();;
}
