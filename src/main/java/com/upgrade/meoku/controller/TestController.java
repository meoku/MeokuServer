package com.upgrade.meoku.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.KMAApiConstants;
import com.upgrade.meoku.weather.api.KMAApiResponseDTO;
import com.upgrade.meoku.weather.api.service.KMAAPIUltraShortTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Value("${WEATHER_API_KEY}")
    private String testApiKey;

    //환경 변수 API_KEY로 호출 해보기
    @GetMapping(value = "/getWeatherData")
    public ResponseEntity<Map<String, Object>> callWeatherApiByEV() throws Exception {
        Map<String, Object> responseBody = new HashMap<>();

        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTime();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", requestDate)
                .queryParam("base_time", requestTime)
                .queryParam("nx", KMAApiConstants.MY_COMPANY_LOCATION_X)
                .queryParam("ny", KMAApiConstants.MY_COMPANY_LOCATION_Y);

        String uriString = uriBuilder.toUriString();
        // 이미 인코딩된 serviceKey 추가 (UriComponentsBuilder 에서 인코딩 가능성이 있음)
        uriString += "&serviceKey=" + testApiKey;

        RestTemplate restTemplate = new RestTemplate();

        // serviceKey가 이미 인코딩 돼있기 때문에 추가 인코딩 방지
        URI uri = URI.create(uriString);
        //API 호출!
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        //성공 아니면 에러 뱉기 (에러일때는 JSON으로 명시해도 xml로 값이 넘어옴)
        if (response.getHeaders().get("Content-Type").contains("text/xml;charset=UTF-8")) throw new Exception();
        //if (!response.getStatusCode().is2xxSuccessful()) throw new Exception();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            Map<String, Object> responseContent = (Map<String, Object>) responseMap.get("response");
            Map<String, Object> body = (Map<String, Object>) responseContent.get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

            // Map<String, Object> 형태의 객체를 -> KMAApiResponseDTO 변환
            List<KMAApiResponseDTO> weatherForecastList = itemList.stream()
                    .map(item -> objectMapper.convertValue(item, KMAApiResponseDTO.class))
                    .collect(Collectors.toList());

            WeatherDataDTO meokuWeather = RequestApiUtil.APIResponseToWeatherDataDTOForUltraShortTermAPI(weatherForecastList);

            responseBody.put("responseBody", meokuWeather);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody.put("error", "Internal server error");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
