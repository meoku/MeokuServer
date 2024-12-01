package com.upgrade.meoku.weather.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.KMAApiConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class KMAApiUVIndex implements  KMAApiService{

    @Value("${WEATHER_API_KEY}")
    private String weatherApiKey;

    @Override
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.UV_INDEX_API_URL)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 300)
                .queryParam("dataType", "JSON")
                .queryParam("areaNo", KMAApiConstants.MY_COMPANY_LOCATION_CODE)
                .queryParam("time", requestDate + reqeustTime);

        String uriString = uriBuilder.toUriString();
        // 이미 인코딩된 serviceKey 추가 (UriComponentsBuilder 에서 인코딩 가능성이 있음)
        uriString += "&serviceKey=" + weatherApiKey;

        RestTemplate restTemplate = new RestTemplate();

        // serviceKey가 이미 인코딩 돼있기 때문에 추가 인코딩 방지
        URI uri = URI.create(uriString);
        //API 호출!
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        //성공 아니면 에러 뱉기 (에러일때는 JSON으로 명시해도 xml로 값이 넘어옴)
        // resultMsg에 NO_DATA가 있으면 데이터 없는것으로 간주
        if(response.getHeaders().get("Content-Type").contains("text/xml;charset=UTF-8")|| response.getBody().contains("\"resultMsg\":\"NO_DATA\"")) return null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            Map<String, Object> responseContent = (Map<String, Object>) responseMap.get("response");
            Map<String, Object> body = (Map<String, Object>) responseContent.get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

            WeatherDataDTO meokuWeather = new WeatherDataDTO();
            String UVIndex = (String) itemList.get(0).get("h0");// 요청시간의 자외선 지수

            meokuWeather.setUvIndex(UVIndex);
            return meokuWeather;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
