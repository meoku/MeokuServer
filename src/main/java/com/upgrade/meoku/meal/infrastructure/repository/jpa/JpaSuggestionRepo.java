package com.upgrade.meoku.meal.infrastructure.repository.jpa;

import com.upgrade.meoku.meal.infrastructure.repository.jpa.entity.SuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JpaSuggestionRepo extends JpaRepository<SuggestionEntity, Long> {
    boolean existsByUserIdAndSuggestedDate(String userId, LocalDate date);
}
