package com.upgrade.meoku.schedule;

import lombok.Data;

@Data
public class WeatherDataDTO {
    private String precipitationType;   // 강수형태
    private String humidity;            // 습도
    private String hourlyPrecipitation; // 1시간강수량
    private String uComponentWind;      // 동서바람성분
    private String windDirection;       // 풍향
    private String vComponentWind;      // 남북바람성분
    private String windSpeed;           // 풍속
    private String temperature;         // 기온
}
