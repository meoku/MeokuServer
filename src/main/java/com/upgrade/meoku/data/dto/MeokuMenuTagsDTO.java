package com.upgrade.meoku.data.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MeokuMenuTagsDTO {

    private Integer menuTagId;
    private Integer menuDetailId;
    private String menuTagName;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;

    // Getters and setters
}
