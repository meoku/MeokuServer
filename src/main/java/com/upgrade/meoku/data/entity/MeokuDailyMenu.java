package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;
import java.sql.Timestamp;

//일자별 식단
@Data
@Entity
@Table(name = "MEOKU_DAILY_MENU")
public class MeokuDailyMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAILY_MENU_ID")
    private Integer dailyMenuId;

    @Column(name = "DATE", nullable = false)
    private Date date;

    @Column(name = "HOLIDAY_FG")
    private String holidayFg;

    @Column(name = "RESTAURANT_OPEN_FG")
    private String restaurantOpenFg;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @jakarta.persistence.Id
    private Long id;

}