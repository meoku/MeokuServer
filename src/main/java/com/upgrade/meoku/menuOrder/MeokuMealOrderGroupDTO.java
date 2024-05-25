package com.upgrade.meoku.menuOrder;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MeokuMealOrderGroupDTO {
    private Integer mealOrderGroupId;
    private Timestamp mealOrderStartDate;
    private Timestamp mealOrderEndDate;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;
}
