package com.upgrade.meoku.data.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MeokuMenuAllergiesDTO {

    private Integer menuAllergyId;
    private Integer menuDetailId;
    private String menuAllergyName;
    private String allergyDescription;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    // Getters and setters
}

