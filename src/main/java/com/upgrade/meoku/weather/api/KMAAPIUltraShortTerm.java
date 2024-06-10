package com.upgrade.meoku.weather.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.config.RequestApiConfig;
import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//초단기 실황
public class KMAAPIUltraShortTerm implements KMAApiService {

    @Override
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception {
//        // 현재 날짜 가져오기
//        String reuqestDate = RequestApiUtil.getTodayDate();
//        // 현재 시간 가져오기
//        String hourOnlyTime = RequestApiUtil.getCurrentTime();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL)
                .queryParam("serviceKey", KMAApiConstants.weatherApiEncodingKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", requestDate)
                .queryParam("base_time", reqeustTime)
                .queryParam("nx", KMAApiConstants.MY_COMPANY_LOCATION_X)
                .queryParam("ny", KMAApiConstants.MY_COMPANY_LOCATION_Y);

        String uriString = uriBuilder.toUriString();

        RestTemplate restTemplate = new RestTemplate();
        //API 호출!
        ResponseEntity<String> response = restTemplate.getForEntity(uriString, String.class);

        System.out.println(response.getBody());
        //성공 아니면 에러 뱉기
        if (!response.getStatusCode().is2xxSuccessful()) throw new Exception();

        return RequestApiUtil.APIResponseToWeatherDataDTO(response);
    }
}
