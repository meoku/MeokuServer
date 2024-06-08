package com.upgrade.meoku.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.config.RequestApiConfig;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class RequestApiUtil {

    private final String WEATHER_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    private final int MY_COMPANY_LOCATION_X = 61;
    private final int MY_COMPANY_LOCATION_Y = 125;

    private final RestTemplate restTemplate;        //날씨 외부 API 호출을 위한 객체
    private final RequestApiConfig requestApiConfig; //날씨 외부 API 호출을 위한 정보

    @Autowired
    public RequestApiUtil(RestTemplate restTemplate, RequestApiConfig requestApiConfig) {
        this.restTemplate = restTemplate;
        this.requestApiConfig = requestApiConfig;
    }

    public WeatherDataDTO getWeatherDataFromApi() throws Exception {
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateForTime = LocalDateTime.now();

        // 날짜를 %Y%m%d 형식으로 포맷팅하기
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
        String todayDate = currentDate.format(formatter1);

        // 시간을 "HH" 형식의 문자열로 포맷
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH");
        String formattedTime = currentDateForTime.format(formatter2);

        // 시간을 시간단위로만 표현
        String hourOnlyTime = formattedTime + "00";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("serviceKey", requestApiConfig.getWeatherApiEncodingKey())
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", todayDate)
                .queryParam("base_time", hourOnlyTime)
                .queryParam("nx", MY_COMPANY_LOCATION_X)
                .queryParam("ny", MY_COMPANY_LOCATION_Y);

        String uriString = uriBuilder.toUriString();
        RestTemplate restTemplate = new RestTemplate();

        //API 호출!
        ResponseEntity<String> response = restTemplate.getForEntity(uriString, String.class);

        System.out.println(response.getBody());
        //성공 아니면 에러 뱉기
        if (!response.getStatusCode().is2xxSuccessful()) throw new Exception();

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
}
