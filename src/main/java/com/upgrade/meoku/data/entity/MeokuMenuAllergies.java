package com.upgrade.meoku.data.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
//메뉴 알러지
@Entity
@Table(name = "MEOKU_MENU_ALLERGIES")
public class MeokuMenuAllergies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ALLERGY_ID")
    private Integer menuAllergyId;

    @Column(name = "MENU_DETAIL_ID", nullable = false)
    private Integer menuDetailId;
    @Column(name = "MENU_ALLERGY_NAME", nullable = false)
    private String menuAllergyName;
    @Column(name = "ALLERGY_DESCRIPTION")
    private String allergyDescription;
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
}
