package com.upgrade.meoku.weather;

import org.springframework.stereotype.Service;

@Service
public class MeokuWeatherService {

    private final MeokuWeatherAPIService meokuWeatherAPIService;
    private final WeatherRepository weatherRepository;


    public MeokuWeatherService(MeokuWeatherAPIService meokuWeatherAPIService,
                               WeatherRepository weatherRepository) {
        this.meokuWeatherAPIService = meokuWeatherAPIService;
        this.weatherRepository = weatherRepository;
    }

    public WeatherData insertWeatherDataFromApi() throws Exception {
        // 날씨 데이터 가져오기
        WeatherDataDTO weatherDataDTO = meokuWeatherAPIService.getUltraShortTermCurrentConditions();

        // API 실행 후 가져온 데이터가 없으면 에러발생 (기상청 API 자체 문제로 가끔 빈 데이터가 올 떄가 있음)
        if(weatherDataDTO.getHumidity() == null) throw new Exception();

        //DTO to Entity
        WeatherData weatherData = new WeatherData();
        weatherData.setPrecipitationType(weatherDataDTO.getPrecipitationType());
        weatherData.setHumidity(weatherDataDTO.getHumidity());
        weatherData.setHourlyPrecipitation(weatherDataDTO.getHourlyPrecipitation());
        weatherData.setUComponentWind(weatherDataDTO.getUComponentWind());
        weatherData.setWindDirection(weatherDataDTO.getWindDirection());
        weatherData.setVComponentWind(weatherDataDTO.getUComponentWind());
        weatherData.setWindSpeed(weatherDataDTO.getWindSpeed());
        weatherData.setTemperature(weatherDataDTO.getTemperature());

        // 날씨 데이터 저장
        return weatherRepository.save(weatherData);
    }

    public WeatherDataDTO getWeatherDataFromDB() throws Exception {

        WeatherData weatherData = weatherRepository.findFirstByOrderByCreatedDateDesc();

        if (weatherData == null) return null;

        //Entity To DTO
        WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
        weatherDataDTO.setPrecipitationType(weatherData.getPrecipitationType());
        weatherDataDTO.setHumidity(weatherData.getHumidity());
        weatherDataDTO.setHourlyPrecipitation(weatherData.getHourlyPrecipitation());
        weatherDataDTO.setUComponentWind(weatherData.getUComponentWind());
        weatherDataDTO.setWindDirection(weatherData.getWindDirection());
        weatherDataDTO.setVComponentWind(weatherData.getVComponentWind());
        weatherDataDTO.setWindSpeed(weatherData.getWindSpeed());
        weatherDataDTO.setTemperature(weatherData.getTemperature());

        return weatherDataDTO;
    }


}
