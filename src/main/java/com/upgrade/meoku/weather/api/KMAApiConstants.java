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

    private KMAApiConstants() {
        // private constructor to prevent instantiation
    }
}