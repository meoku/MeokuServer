package com.upgrade.meoku.menuOrder;

import jakarta.persistence.*;
import lombok.Data;


import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "MEOKU_MEAL_ORDER")
public class MeokuMealOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ORDER_ID")
    private Integer mealOrderId;

    @ManyToOne
    @JoinColumn(name = "MEAL_ORDER_GROUP_ID")
    private MeokuMealOrderGroup meokuMealOrderGroup;

    @Column(name = "MEAL_ORDER")
    private Integer mealOrder;

    @Column(name = "MEAL_TIME")
    private Timestamp mealTime;

    @Column(name = "MEAL_TARGET", length = 50)
    private String mealTarget;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CREATED_BY", length = 20)
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPDATED_BY", length = 20)
    private String updatedBy;
}
