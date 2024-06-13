package com.upgrade.meoku.weather;

import com.upgrade.meoku.util.RequestApiUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;
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
    @Transactional
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
    /**
     * 기존 저장된 (or 새로만든)weatherData Entity 객체의 필드를 API로 받아온 날씨dto 객체의 필드 값으로 업데이트합니다.
     * 날씨dto 필드값이 nulldㅣ 아닐떄나 필드값이 다를때 업데이트
     *
     * @param weatherData 업데이트할 WeatherData 객체
     * @param dto 새로운 값을 담고 있는 WeatherDataDTO 객체
     */
    public static void updateWeatherDataIfDifferent(WeatherData weatherData, WeatherDataDTO dto) {
        // WeatherData 클래스의 모든 필드
        Field[] fields = WeatherData.class.getDeclaredFields();
        // WeatherDataDTO 클래스의 모든 필드
        Field[] dtoFields = WeatherDataDTO.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                if ("weatherId".equals(field.getName())) continue;  // Id는 영속성의 문제로 당연히 건나뛰기

                field.setAccessible(true);                          // private 필드에 접근할 수 있도록 설정
                Object weatherDataValue = field.get(weatherData);   // weatherData 객체에서 현재 필드의 값을 가져옴

                for (Field dtoField : dtoFields) {
                    // 필드 이름이 같은 경우에만 처리
                    if (field.getName().equals(dtoField.getName())) {
                        dtoField.setAccessible(true);       // private 필드에 접근할 수 있도록 설정
                        Object dtoValue = dtoField.get(dto);// dto 객체에서 현재 필드의 값을 가져옴

                        // dto의 값이 null이 아니고 weatherData의 값과 다를 경우 업데이트
                        if (dtoValue != null && !Objects.equals(weatherDataValue, dtoValue)) {
                            field.set(weatherData, dtoValue);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
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
