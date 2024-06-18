package com.upgrade.meoku.mapper;

import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WatherDataMapper {
    // 이렇게 사용하는 이유는 매퍼는 컴파일 할때 해당 Class 내용을 구현하며 싱글턴 패턴으로 제공하기 때문!
    WatherDataMapper INSTANCE = Mappers.getMapper(WatherDataMapper.class);

    WeatherDataDTO weatherDataToWeatherDataDTO(WeatherData weatherData);
    WeatherData weatherDataDTOToWeatherData(WeatherDataDTO weatherDataDTO);
}
