package com.upgrade.meoku.weather;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherData, Integer> {
    //가장 최신 날씨데이터 가져오기
    WeatherData findFirstByOrderByCreatedDateDesc();
}
