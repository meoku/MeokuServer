package com.upgrade.meoku.weather;

import lombok.Data;

@Data
public class WeatherDataDTO {
    /* 단기 예보 */
    private String precipitationProbability;    // 강수확률
    private String oneHourSnowfall;             // 1시간 신적설(쌓인 눈 양)
    private String skyCondition;                // 하늘상태
    private String oneHourTemperature;          // 1시간 기온
    private String dailyMinimumTemperature;     // 일 최저기온
    private String dailyMaximumTemperature;     // 일 최고기온
    /* 초단기 실황 */
    private String precipitationType;   // 강수형태
    private String humidity;            // 습도
    private String hourlyPrecipitation; // 1시간강수량
    private String uComponentWind;      // 동서바람성분
    private String windDirection;       // 풍향
    private String vComponentWind;      // 남북바람성분
    private String windSpeed;           // 풍속
    private String temperature;         // 기온
    /* 초단기 예보 */
    // 당장은 필요 없어보임
}
