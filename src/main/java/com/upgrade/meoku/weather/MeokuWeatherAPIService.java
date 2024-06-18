package com.upgrade.meoku.weather;

import com.upgrade.meoku.config.RequestApiConfig;
import com.upgrade.meoku.util.RequestApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MeokuWeatherAPIService{ // 이거 이제 안씀

    // 단기예보조회
    private String SHORT_TERM_FORECAST_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    // 초단기실황조회
    private String ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    // 초단기예보조회
    private String ULTRA_SHORT_TERM_FORECAST_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    // 도곡동 위치정보
    private final int MY_COMPANY_LOCATION_X = 61;
    private final int MY_COMPANY_LOCATION_Y = 125;

    private final RestTemplate restTemplate;        //날씨 외부 API 호출을 위한 객체
    private final RequestApiConfig requestApiConfig; //날씨 외부 API 호출을 위한 정보

    @Autowired
    public MeokuWeatherAPIService(RestTemplate restTemplate, RequestApiConfig requestApiConfig) {
        this.restTemplate = restTemplate;
        this.requestApiConfig = requestApiConfig;
    }

    // 초단기실황조회 (안씀)
    public WeatherDataDTO getUltraShortTermCurrentConditions() throws Exception {
        // 현재 날짜 가져오기
        String todayDate = RequestApiUtil.getTodayDate();
        // 현재 시간 가져오기
        String hourOnlyTime = RequestApiUtil.getCurrentTime();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL)
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
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        //성공 아니면 에러 뱉기
        if (!response.getStatusCode().is2xxSuccessful()) throw new Exception();

        return RequestApiUtil.APIResponseToWeatherDataDTO(response);

    }
    // 단기예보조회 로직 (안씀)
    public WeatherDataDTO getShortTermForecast() throws Exception {
        // 현재 날짜 가져오기
        String todayDate = RequestApiUtil.getTodayDate();
        // 현재 시간 가져오기
        String hourOnlyTime = "0200"; //최고기온은 15시, 최저기온은 06시부터 이기 때문에

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(SHORT_TERM_FORECAST_API_URL)
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

        //성공 아니면 에러 뱉기
        if (!response.getStatusCode().is2xxSuccessful()) throw new Exception();

        return RequestApiUtil.APIResponseToWeatherDataDTO(response);
    }

    // 초단기예보조회 로직
    public String getUltraShortTermForecast() {
        return null;
    }

}
