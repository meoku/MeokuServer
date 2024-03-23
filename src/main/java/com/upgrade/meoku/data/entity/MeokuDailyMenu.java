package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
    private Timestamp date;

    @Column(name = "HOLIDAY_FG")
    private String holidayFg;

    @Column(name = "RESTAURANT_OPEN_FG")
    private String restaurantOpenFg;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    // 1 : N
    @OneToMany(mappedBy = "meokuDailyMenu", cascade = CascadeType.ALL)
    private List<MeokuDetailedMenu> detailedMenuList;

}