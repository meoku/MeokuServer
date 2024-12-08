package com.upgrade.meoku.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeokuUserRepository extends JpaRepository<MeokuUser, Integer> {
    Optional<MeokuUser> findMeokuUserById(String id);
}
