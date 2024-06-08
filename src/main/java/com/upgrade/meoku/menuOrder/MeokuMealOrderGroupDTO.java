package com.upgrade.meoku.menuOrder;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class MeokuMealOrderGroupDTO {
    private Integer mealOrderGroupId;
    private LocalDate mealOrderStartDate;
    private LocalDate mealOrderEndDate;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;
}
