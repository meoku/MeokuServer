package com.upgrade.meoku.meal.infrastructure.repository.jpa;

import com.upgrade.meoku.meal.domain.model.Suggestion;
import com.upgrade.meoku.meal.domain.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class JpaSuggestionRepositoryAdapter implements SuggestionRepository {
    private final JpaSuggestionRepo suggestionRepo;

    @Override
    public boolean existsByUserAndDate(String userId, LocalDate date) {
        return suggestionRepo.existsByUserIdAndSuggestedDate(userId, date);
    }

    @Override
    public void save(Suggestion suggestion) {

    }
}
