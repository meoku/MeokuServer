package com.upgrade.meoku.weather.api;

import lombok.Data;

@Data
public class KMAApiResponseDTO {
    //공통
    private String baseDate;
    private String baseTime;
    private String category;
    private int nx;
    private int ny;
    //초단기실황
    private String obsrValue;
    //단기에보 응답값
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
}
