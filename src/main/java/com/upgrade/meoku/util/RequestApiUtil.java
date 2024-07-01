package com.upgrade.meoku.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.config.RequestApiConfig;
import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.KMAApiResponseDTO;
import com.upgrade.meoku.weather.api.KMAApiShortTermResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RequestApiUtil {

    // 정적 메소드 사용을 위해
    private RequestApiUtil(){}

    // 단기 예보 호출 가능한 시간대를 정의한 Enum
    enum ShortTermForecastRequestAvailableHour {
        HOUR_2(2),
        HOUR_5(5),
        HOUR_8(8),
        HOUR_11(11),
        HOUR_14(14),
        HOUR_17(17),
        HOUR_20(20),
        HOUR_23(23);

        private final int hour;

        ShortTermForecastRequestAvailableHour(int hour) {
            this.hour = hour;
        }

        public int getHour() {
            return hour;
        }
    }

    // 기상철 API 호출을 위한 현재 날짜 가져오기
    public static String getTodayDate(){
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // 날짜를 %Y%m%d 형식으로 포맷팅하기
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
        String todayDate = currentDate.format(formatter1);

        return todayDate;
    }
    // 기상청 API 호출을 위한 현재 시간 가져오기 ex) 0500
    public static String getCurrentTime(){
        // 현재 (날짜) + 시간 가져오기
        LocalDateTime currentDateForTime = LocalDateTime.now();

        // 시간을 "HH" 형식의 문자열로 포맷
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH");
        String formattedTime = currentDateForTime.format(formatter2);

        // 시간을 시간단위로만 표현
        String hourOnlyTime = formattedTime + "00";

        return hourOnlyTime;
    }
    // 기상청 API 호출을 위한 현재 시간 가져오기2 ex) 05
    public static String getCurrentTimeToShort(){
        // 현재 (날짜) + 시간 가져오기
        LocalDateTime currentDateForTime = LocalDateTime.now();

        // 시간을 "HH" 형식의 문자열로 포맷
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH");
        String formattedTime = currentDateForTime.format(formatter2);

        return formattedTime;
    }
    // 단기에보 호출을 위한 정해진 시간 가져오기 - 기상청 API
    public static String getRequestTimeForShortTermForecastRequest(){
        // 현재 시간
        LocalTime currentTime = LocalTime.now();

        // 현재 시간보다 이전인 가장 최근의 시간대를 찾음
        int recentHour = 2;
        for (ShortTermForecastRequestAvailableHour hour : ShortTermForecastRequestAvailableHour.values()) {
            if (hour.getHour() <= currentTime.getHour()) {
                recentHour = hour.getHour();
            } else {
                break;
            }
        }

        // 원하는 날짜와 시간을 지정하여 LocalDateTime을 만듭니다.
        LocalDateTime customDateTime = LocalDateTime.of(2024, 6, 9, recentHour, 0);
        String requestTime = customDateTime.format(DateTimeFormatter.ofPattern("HHmm"));

        return requestTime;
    }

    //기상청 API 호출 후 받은 데이터로 원하는 데이터 파싱하기(안씀이제)
    public static WeatherDataDTO APIResponseToWeatherDataDTO(ResponseEntity<String> response){
        WeatherDataDTO weatherDataDTO = new WeatherDataDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            Map<String, Object> responseContent = (Map<String, Object>) responseMap.get("response");
            Map<String, Object> body = (Map<String, Object>) responseContent.get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

            for (Map<String, Object> item : itemList) {
                String category = (String) item.get("category");

                switch (category){
                    //단기 예보 부분
                    case "POP": //강수확률
                        weatherDataDTO.setPrecipitationProbability((String) item.get("fcstValue"));
                        break;
                    case "SNO": //신적설
                        weatherDataDTO.setOneHourSnowfall((String) item.get("fcstValue"));
                        break;
                    case "SKY": //하늘상태 1-맑음 3-구름많음 4-흐림
                        weatherDataDTO.setSkyCondition((String) item.get("fcstValue"));
                        break;
                    case "TMP": //한시간기온
                        weatherDataDTO.setOneHourTemperature((String) item.get("fcstValue"));
                        break;
                    case "TMN": //최저기온
                        weatherDataDTO.setDailyMinimumTemperature((String) item.get("fcstValue"));
                        break;
                    case "TMX": //최고기온
                        weatherDataDTO.setDailyMaximumTemperature((String) item.get("fcstValue"));
                        break;
                    // 초단기 실황 부분
                    case "PTY":
                        weatherDataDTO.setPrecipitationType((String) item.get("obsrValue"));
                        break;
                    case "REH":
                        weatherDataDTO.setHumidity((String) item.get("obsrValue"));
                        break;
                    case "RN1":
                        weatherDataDTO.setHourlyPrecipitation((String) item.get("obsrValue"));
                        break;
                    case "UUU":
                        weatherDataDTO.setUComponentWind((String) item.get("obsrValue"));
                        break;
                    case "VEC":
                        weatherDataDTO.setWindDirection((String) item.get("obsrValue"));
                        break;
                    case "VVV":
                        weatherDataDTO.setVComponentWind((String) item.get("obsrValue"));
                        break;
                    case "WSD":
                        weatherDataDTO.setWindSpeed((String) item.get("obsrValue"));
                        break;
                    case "T1H":
                        weatherDataDTO.setTemperature((String) item.get("obsrValue"));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weatherDataDTO;
    }

    //초단기실황 기상청 API 호출 후 받은 데이터로 원하는 데이터 파싱하기
    public static WeatherDataDTO APIResponseToWeatherDataDTOForUltraShortTermAPI(List<KMAApiResponseDTO> itemList){
        WeatherDataDTO weatherDataDTO = new WeatherDataDTO();

        for (KMAApiResponseDTO item : itemList) {
            String category = item.getCategory();

            switch (category){
                // 초단기 실황 부분
                case "PTY":
                    weatherDataDTO.setPrecipitationType(item.getObsrValue());
                    break;
                case "REH":
                    weatherDataDTO.setHumidity(item.getObsrValue());
                    break;
                case "RN1":
                    weatherDataDTO.setHourlyPrecipitation(item.getObsrValue());
                    break;
                case "UUU":
                    weatherDataDTO.setUComponentWind(item.getObsrValue());
                    break;
                case "VEC":
                    weatherDataDTO.setWindDirection(item.getObsrValue());
                    break;
                case "VVV":
                    weatherDataDTO.setVComponentWind(item.getObsrValue());
                    break;
                case "WSD":
                    weatherDataDTO.setWindSpeed(item.getObsrValue());
                    break;
                case "T1H":
                    weatherDataDTO.setTemperature(item.getObsrValue());
                    break;
            }
        }
        return weatherDataDTO;
    }

    //단기예보 기상청 API 호출 후 받은 데이터로 원하는 데이터 파싱하기
    public static WeatherDataDTO APIResponseToWeatherDataDTOForShortTermAPI(List<KMAApiShortTermResponseDTO> responseDTOList){
        WeatherDataDTO weatherDataDTO = new WeatherDataDTO();

        for (KMAApiShortTermResponseDTO item : responseDTOList) {
            String category = item.getCategory();

            switch (category){
                //단기 예보 부분
                case "POP": //강수확률
                    weatherDataDTO.setPrecipitationProbability(item.getFcstValue());
                    break;
                case "SNO": //신적설
                    weatherDataDTO.setOneHourSnowfall(item.getFcstValue());
                    break;
                case "SKY": //하늘상태 1-맑음 3-구름많음 4-흐림
                    weatherDataDTO.setSkyCondition(item.getFcstValue());
                    break;
                case "TMP": //한시간기온
                    weatherDataDTO.setOneHourTemperature(item.getFcstValue());
                    break;
                case "TMN": //최저기온
                    weatherDataDTO.setDailyMinimumTemperature(item.getFcstValue());
                    break;
                case "TMX": //최고기온
                    weatherDataDTO.setDailyMaximumTemperature(item.getFcstValue());
                    break;
            }
        }
        return weatherDataDTO;
    }

    //Entity To DTO
    public static WeatherDataDTO WeatherDataToWeatherDateDTO(WeatherData weatherData){
        WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
        // 단기 예보
//        weatherDataDTO.setWeatherId(weatherData.getWeatherId());
        weatherDataDTO.setWeatherDate(weatherData.getWeatherDate());
        weatherDataDTO.setPrecipitationProbability(weatherData.getPrecipitationProbability());
        weatherDataDTO.setOneHourSnowfall(weatherData.getOneHourSnowfall());
        weatherDataDTO.setSkyCondition(weatherData.getSkyCondition());
        weatherDataDTO.setOneHourTemperature(weatherData.getOneHourTemperature());
        weatherDataDTO.setDailyMinimumTemperature(weatherData.getDailyMinimumTemperature());
        weatherDataDTO.setDailyMaximumTemperature(weatherData.getDailyMaximumTemperature());
        // 초단기 실황
        weatherDataDTO.setTemperature(weatherData.getTemperature());
        weatherDataDTO.setPrecipitationType(weatherData.getPrecipitationType());
        weatherDataDTO.setHumidity(weatherData.getHumidity());
        weatherDataDTO.setHourlyPrecipitation(weatherData.getHourlyPrecipitation());
        weatherDataDTO.setUComponentWind(weatherData.getUComponentWind());
        weatherDataDTO.setWindDirection(weatherData.getWindDirection());
        weatherDataDTO.setVComponentWind(weatherData.getVComponentWind());
        weatherDataDTO.setWindSpeed(weatherData.getWindSpeed());

        return weatherDataDTO;
    }
    //DTO to Entity
    public static WeatherData WeatherDataDTOToWeatherData(WeatherDataDTO weatherDataDTO){
        WeatherData weatherData = new WeatherData();
        // 단기 예보
//        weatherData.setWeatherId(weatherDataDTO.getWeatherId());
        weatherData.setWeatherDate(weatherDataDTO.getWeatherDate());
        weatherData.setPrecipitationProbability(weatherDataDTO.getPrecipitationProbability());
        weatherData.setOneHourSnowfall(weatherDataDTO.getOneHourSnowfall());
        weatherData.setSkyCondition(weatherDataDTO.getSkyCondition());
        weatherData.setOneHourTemperature(weatherDataDTO.getOneHourTemperature());
        weatherData.setDailyMinimumTemperature(weatherDataDTO.getDailyMinimumTemperature());
        weatherData.setDailyMaximumTemperature(weatherDataDTO.getDailyMaximumTemperature());
        // 초단기 실황
        weatherData.setTemperature(weatherDataDTO.getTemperature());
        weatherData.setPrecipitationType(weatherDataDTO.getPrecipitationType());
        weatherData.setHumidity(weatherDataDTO.getHumidity());
        weatherData.setHourlyPrecipitation(weatherDataDTO.getHourlyPrecipitation());
        weatherData.setUComponentWind(weatherDataDTO.getUComponentWind());
        weatherData.setWindDirection(weatherDataDTO.getWindDirection());
        weatherData.setVComponentWind(weatherDataDTO.getVComponentWind());
        weatherData.setWindSpeed(weatherDataDTO.getWindSpeed());
        return weatherData;
    }
}
