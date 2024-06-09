package com.upgrade.meoku.weather.api;

import com.upgrade.meoku.weather.WeatherDataDTO;

public interface MeokuWeatherApiService {
    // 단기예보조회
    String SHORT_TERM_FORECAST_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    // 초단기실황조회
    String ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    // 초단기예보조회
    String ULTRA_SHORT_TERM_FORECAST_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    // 도곡동 위치정보
    final int MY_COMPANY_LOCATION_X = 61;
    final int MY_COMPANY_LOCATION_Y = 125;

    public WeatherDataDTO requestWeatherApi();
}
