package com.upgrade.meoku.menuOrder;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "MEOKU_MEAL_ORDER_GROUP")
public class MeokuMealOrderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ORDER_GROUP_ID")
    private Integer mealOrderGroupId;

    @Column(name = "MEAL_ORDER_START_DATE")
    private LocalDate mealOrderStartDate;

    @Column(name = "MEAL_ORDER_END_DATE")
    private LocalDate mealOrderEndDate;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
