package com.upgrade.meoku.mealmenu.data.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SubMenuAllergiesDTO {
    private Integer menuAllergyId;
    private Integer menuItemId;
    private String menuAllergyName;
    private String allergyDescription;
}
