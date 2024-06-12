package com.upgrade.meoku.weather;

import com.upgrade.meoku.util.RequestApiUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MeokuWeatherService {

    private final MeokuWeatherAPIService meokuWeatherAPIService;
    private final WeatherRepository weatherRepository;


    public MeokuWeatherService(MeokuWeatherAPIService meokuWeatherAPIService,
                               WeatherRepository weatherRepository) {
        this.meokuWeatherAPIService = meokuWeatherAPIService;
        this.weatherRepository = weatherRepository;
    }


    //날씨 데이터 최신화
    public WeatherData updateWeatherDataFromApi(LocalDate weatherDateForSearch, WeatherDataDTO updateWeatherDataDTO) throws Exception {
        // 입력된 날짜로 날씨 데이터 가져오기
        Optional<WeatherData> searchedWeatherData = weatherRepository.findByWeatherDate(weatherDateForSearch);

        WeatherData updatedweatherData = null;

        //해당 날짜로 값이 존재하지 않을 때 생성
        if(!searchedWeatherData.isPresent()) {
            WeatherData newWeatherData = new WeatherData();
            newWeatherData.setWeatherDate(weatherDateForSearch);
            updatedweatherData = weatherRepository.save(newWeatherData);
        }else{
            updatedweatherData = searchedWeatherData.get();
        }
        // API로 가져온 최신 날씨 데이터를 변경된 값이 있을 시 Update
        updateWeatherDataIfDifferent(updatedweatherData, updateWeatherDataDTO);

        return weatherRepository.save(updatedweatherData);
    }

    private void updateWeatherDataIfDifferent(WeatherData weatherData, WeatherDataDTO dto) {
        if (!weatherData.getPrecipitationProbability().equals(dto.getPrecipitationProbability())) {
            weatherData.setPrecipitationProbability(dto.getPrecipitationProbability());
        }
        if (!weatherData.getOneHourSnowfall().equals(dto.getOneHourSnowfall())) {
            weatherData.setOneHourSnowfall(dto.getOneHourSnowfall());
        }
        if (!weatherData.getSkyCondition().equals(dto.getSkyCondition())) {
            weatherData.setSkyCondition(dto.getSkyCondition());
        }
        if (!weatherData.getOneHourTemperature().equals(dto.getOneHourTemperature())) {
            weatherData.setOneHourTemperature(dto.getOneHourTemperature());
        }
        if (!weatherData.getDailyMinimumTemperature().equals(dto.getDailyMinimumTemperature())) {
            weatherData.setDailyMinimumTemperature(dto.getDailyMinimumTemperature());
        }
        if (!weatherData.getDailyMaximumTemperature().equals(dto.getDailyMaximumTemperature())) {
            weatherData.setDailyMaximumTemperature(dto.getDailyMaximumTemperature());
        }
        if (!weatherData.getPrecipitationType().equals(dto.getPrecipitationType())) {
            weatherData.setPrecipitationType(dto.getPrecipitationType());
        }
        if (!weatherData.getHumidity().equals(dto.getHumidity())) {
            weatherData.setHumidity(dto.getHumidity());
        }
        if (!weatherData.getHourlyPrecipitation().equals(dto.getHourlyPrecipitation())) {
            weatherData.setHourlyPrecipitation(dto.getHourlyPrecipitation());
        }
        if (!weatherData.getUComponentWind().equals(dto.getUComponentWind())) {
            weatherData.setUComponentWind(dto.getUComponentWind());
        }
        if (!weatherData.getWindDirection().equals(dto.getWindDirection())) {
            weatherData.setWindDirection(dto.getWindDirection());
        }
        if (!weatherData.getVComponentWind().equals(dto.getVComponentWind())) {
            weatherData.setVComponentWind(dto.getVComponentWind());
        }
        if (!weatherData.getWindSpeed().equals(dto.getWindSpeed())) {
            weatherData.setWindSpeed(dto.getWindSpeed());
        }
        if (!weatherData.getTemperature().equals(dto.getTemperature())) {
            weatherData.setTemperature(dto.getTemperature());
        }
    }

    // 안씀
    public WeatherData insertWeatherDataFromApi() throws Exception {
        // 날씨 데이터 가져오기
        WeatherDataDTO weatherDataDTO = meokuWeatherAPIService.getUltraShortTermCurrentConditions();
        // API 실행 후 가져온 데이터가 없으면 에러발생 (기상청 API 자체 문제로 가끔 빈 데이터가 올 떄가 있음)
        if(weatherDataDTO.getHumidity() == null) throw new Exception();
        //DTO to Entity
        WeatherData weatherData = RequestApiUtil.WeatherDataDTOToWeatherData(weatherDataDTO);
        // 날씨 데이터 저장
        return weatherRepository.save(weatherData);
    }

    // 안씀
    public WeatherDataDTO getWeatherDataFromDB() throws Exception {
        WeatherData weatherData = weatherRepository.findFirstByOrderByCreatedDateDesc();

        if (weatherData == null) return null;
        //Entity To DTO
        WeatherDataDTO weatherDataDTO = RequestApiUtil.WeatherDataToWeatherDateDTO(weatherData);

        return weatherDataDTO;
    }


}
