package com.upgrade.meoku.weather.api.service;

import com.upgrade.meoku.weather.WeatherDataDTO;

public interface KMAApiService {
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception;
}
