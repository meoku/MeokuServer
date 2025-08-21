package com.upgrade.meoku.meal.domain.repository;

import com.upgrade.meoku.meal.domain.model.Suggestion;
import java.time.LocalDate;

public interface SuggestionRepository {
    boolean existsByUserAndDate(String userId, LocalDate date);
    void save(Suggestion suggestion);
}
