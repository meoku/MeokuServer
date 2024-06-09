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
    //날씨 데이터 업데이트
    public WeatherData updateWeatherDataFromApi(LocalDate weatherDateForSearch) throws Exception {
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

        // API호출(단기, 초단기실황, 초단기예보)
        // 디자인패턴 써서 객체 실행으로 결과 값 받아오기!
        // 변경된 값 있으면 Update - 받아온 DTO 값으로 확인 후 처리


        return weatherRepository.save(updatedweatherData);
    }
    public WeatherDataDTO getWeatherDataFromDB() throws Exception {
        WeatherData weatherData = weatherRepository.findFirstByOrderByCreatedDateDesc();

        if (weatherData == null) return null;
        //Entity To DTO
        WeatherDataDTO weatherDataDTO = RequestApiUtil.WeatherDataToWeatherDateDTO(weatherData);

        return weatherDataDTO;
    }


}
