package com.upgrade.meoku.weather.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.config.RequestApiConfig;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.KMAApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class KMAApiPerTemp implements KMAApiService{

    private final RequestApiConfig requestApiConfig; //날씨 외부 API 호출을 위한 정보

    public KMAApiPerTemp(RequestApiConfig requestApiConfig) {
        this.requestApiConfig = requestApiConfig;
    }

    @Override
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.PERCIVED_TEMP_API_URL)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 300) //하루 예보는 이론상 최대 96개
                .queryParam("dataType", "JSON")
                .queryParam("areaNo", KMAApiConstants.MY_COMPANY_LOCATION_CODE)
                .queryParam("time", requestDate + reqeustTime)
                .queryParam("requestCode", "A41"); //노인 대상 체감온도 (일반적인 체감온도로 많이 쓰이는것 같음)


        String uriString = uriBuilder.toUriString();
        // 이미 인코딩된 serviceKey 추가 (UriComponentsBuilder 에서 인코딩 가능성이 있음)
        uriString += "&serviceKey=" + requestApiConfig.getWeatherApiEncodingKey();

        RestTemplate restTemplate = new RestTemplate();

        // serviceKey가 이미 인코딩 돼있기 때문에 추가 인코딩 방지
        URI uri = URI.create(uriString);
        //API 호출!
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        //성공 아니면 에러 뱉기 (에러일때는 JSON으로 명시해도 xml로 값이 넘어옴)
        if(response.getHeaders().get("Content-Type").contains("text/xml;charset=UTF-8")) throw new Exception();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            Map<String, Object> responseContent = (Map<String, Object>) responseMap.get("response");
            Map<String, Object> body = (Map<String, Object>) responseContent.get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

            WeatherDataDTO meokuWeather = new WeatherDataDTO();
            String percivedTemp = (String) itemList.get(0).get("h1");// 요청시간의 체감온도

            meokuWeather.setPercivedTemperature(percivedTemp);
            return meokuWeather;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
