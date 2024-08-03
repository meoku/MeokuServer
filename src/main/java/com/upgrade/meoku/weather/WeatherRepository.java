package com.upgrade.meoku.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherData, Integer> {
    //원하는 날짜의 날씨 데이터 가져오기
    Optional<WeatherData> findByWeatherDate(LocalDate weatherDate);
    //가지고있는 최신날짜를 가져오기
    Optional<WeatherData> findFirstByOrderByCreatedDateDesc();
}
