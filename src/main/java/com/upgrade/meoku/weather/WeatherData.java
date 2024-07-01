package com.upgrade.meoku.weather;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "MEOKU_WEATHER")
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEATHER_ID")
    private Integer weatherId;
    @Column(name = "WEATHER_DATE", unique = true, nullable = false)
    private LocalDate weatherDate;
    /* 단기 예보 */
    @Column(name = "PRECIPITATION_PROBABILITY")
    private String precipitationProbability;    // 강수확률
    @Column(name = "HOURLY_SNOWFALL")
    private String oneHourSnowfall;             // 1시간 신적설(쌓인 눈 양)
    @Column(name = "SKY_CONDITION")
    private String skyCondition;                // 하늘상태
    @Column(name = "HOURLY_TEMPERATURE")
    private String oneHourTemperature;          // 1시간 기온
    @Column(name = "DAILY_MIN_TEMPERATURE")
    private String dailyMinimumTemperature;     // 일 최저기온
    @Column(name = "DAILY_MAX_TEMPERATURE")
    private String dailyMaximumTemperature;     // 일 최고기온
    /* 초단기 실황 */
    @Column(name = "PRECIPITATION_TYPE")
    private String precipitationType;   // 강수형태
    @Column(name = "HUMIDITY")
    private String humidity;            // 습도
    @Column(name = "HOURLY_PRECIPITATION")
    private String hourlyPrecipitation; // 1시간강수량
    @Column(name = "U_COMPONENT_WIND")
    private String uComponentWind;      // 동서바람성분
    @Column(name = "WIND_DIRECTION")
    private String windDirection;       // 풍향
    @Column(name = "V_COMPONENT_WIND")
    private String vComponentWind;      // 남북바람성분
    @Column(name = "WIND_SPEED")
    private String windSpeed;           // 풍속
    @Column(name = "TEMPERATURE")
    private String temperature;         // 기온
    /*자외선지수*/
    @Column(name = "UV_INDEX")
    private String uvIndex;             //자외선지수

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
