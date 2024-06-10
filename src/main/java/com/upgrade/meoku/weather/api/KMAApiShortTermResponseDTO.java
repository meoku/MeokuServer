package com.upgrade.meoku.weather.api;

import lombok.Data;

@Data
public class KMAApiShortTermResponseDTO {
    //단기에보 응답값
    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private int nx;
    private int ny;
}
