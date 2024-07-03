package com.upgrade.meoku.weather;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;

@ComponentScan
@Schema(description = "Weather Data DTO")
@Data
public class WeatherDataDTO {
    @Schema(description = "날씨 ID")
    private int weatherId;
    @Schema(description = "날씨 날짜 - 날씨 데이터의 기준 날짜")
    private LocalDate weatherDate;
    /* 단기 예보 */
    @Schema(description = "강수확률")
    private String precipitationProbability;
    @Schema(description = "1시간 신적설(쌓인 눈 양)")
    private String oneHourSnowfall;
    @Schema(description = "하늘 상태 / 코드 : 맑음(1), 구름많음(3), 흐림(4)")
    private String skyCondition;
    @Schema(description = "1시간 기온")
    private String oneHourTemperature;
    @Schema(description = "일 최저 기온")
    private String dailyMinimumTemperature;
    @Schema(description = "일 최고 기온")
    private String dailyMaximumTemperature;
    /* 초단기 실황 */
    @Schema(description = "강수 형태 / 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)")
    private String precipitationType;
    @Schema(description = "습도")
    private String humidity;
    @Schema(description = "1시간 강수량 / -, null, 0값은 ‘강수없음’")
    private String hourlyPrecipitation;
    @Schema(description = "동서 바람 성분")
    private String uComponentWind;
    @Schema(description = "풍향")
    private String windDirection;
    @Schema(description = "남북 바람 성분")
    private String vComponentWind;
    @Schema(description = "풍속")
    private String windSpeed;
    @Schema(description = "기온")
    private String temperature;
    /*자외선지수*/
    @Schema(description = "0~2:낮음/3~5:보통/6~7:높음/8~10:매우높음/11이상:위험")
    private String uvIndex;
    @Schema(description = "- : 29 미만 / 관심 : 29 이상 31 미만소 / 주의 : 31 이상 34 미만 / 경고 : 34 이상 37 미만 /위험 : 37 이상")
    private String percivedTemperature; //체감온도
}
