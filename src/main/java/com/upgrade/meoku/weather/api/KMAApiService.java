package com.upgrade.meoku.weather.api;

import com.upgrade.meoku.weather.WeatherDataDTO;

public interface KMAApiService {
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception;
}
