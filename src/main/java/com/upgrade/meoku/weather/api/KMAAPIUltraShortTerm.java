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

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//초단기 실황
@Component
public class KMAAPIUltraShortTerm implements KMAApiService {

    private final RequestApiConfig requestApiConfig; //날씨 외부 API 호출을 위한 정보

    @Autowired
    public KMAAPIUltraShortTerm(RequestApiConfig requestApiConfig) {
        this.requestApiConfig = requestApiConfig;
    }

    @Override
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL)
//                .queryParam("serviceKey", requestApiConfig.getWeatherApiEncodingKey())
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", requestDate)
                .queryParam("base_time", reqeustTime)
                .queryParam("nx", KMAApiConstants.MY_COMPANY_LOCATION_X)
                .queryParam("ny", KMAApiConstants.MY_COMPANY_LOCATION_Y);

        String uriString = uriBuilder.toUriString();
        // 이미 인코딩된 serviceKey 추가 (UriComponentsBuilder 에서 인코딩 가능성이 있음)
        uriString += "&serviceKey=" + requestApiConfig.getWeatherApiEncodingKey();

        RestTemplate restTemplate = new RestTemplate();

        // serviceKey가 이미 인코딩 돼있기 때문에 추가 인코딩 방지
        URI uri = URI.create(uriString);
        //API 호출!
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        //성공 아니면 에러 뱉기
        if (!response.getStatusCode().is2xxSuccessful()) throw new Exception();

        return RequestApiUtil.APIResponseToWeatherDataDTO(response);
    }
}
