package com.upgrade.meoku.weather.api;

public class KMAApiConstants {
    //단기예보
    public static final String SHORT_TERM_FORECAST_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    //초단기실황
    public static final String ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    //초단기예보
    public static final String ULTRA_SHORT_TERM_FORECAST_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    //도곡동 위치정보
    public static final int MY_COMPANY_LOCATION_X = 61;
    public static final int MY_COMPANY_LOCATION_Y = 125;

    //인증키(여기에 두는게 맞나?)
    public static final String weatherApiEncodingKey = "3FZCxvPHQygrXcCm2MEXJU57MJOngj9Gn0IAo6WO8qlVBwIdkkqeFjR2ckhoiMvr%2Braz5xBVwUd9Po72sBy3JQ%3D%3D";
    private KMAApiConstants() {
        // private constructor to prevent instantiation
    }
}