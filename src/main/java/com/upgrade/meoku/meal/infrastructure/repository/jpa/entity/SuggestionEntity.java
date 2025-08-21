package com.upgrade.meoku.meal.infrastructure.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SuggestionEntity {
    @Id
    private Long id;
}
