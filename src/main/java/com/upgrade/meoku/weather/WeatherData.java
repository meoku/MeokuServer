package com.upgrade.meoku.weather;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "MEOKU_WEATHER")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEATHER_ID")
    private Integer weatherId;
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
