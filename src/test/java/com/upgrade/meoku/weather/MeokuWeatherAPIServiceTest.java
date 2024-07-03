package com.upgrade.meoku.weather;

import com.upgrade.meoku.config.RequestApiConfig;
import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.api.KMAAPIShortTerm;
import com.upgrade.meoku.weather.api.KMAAPIUltraShortTerm;
import com.upgrade.meoku.weather.api.KMAApiConstants;
import com.upgrade.meoku.weather.api.KMAApiUVIndex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

import static com.upgrade.meoku.weather.api.KMAApiConstants.*;

@SpringBootTest
public class MeokuWeatherAPIServiceTest {

    @Autowired
    private MeokuWeatherAPIService meokuWeatherAPIService;
    @Autowired
    private MeokuWeatherService meokuWeatherService;

    @Autowired
    private KMAAPIUltraShortTerm kmaAPIUltraShortTerm;
    @Autowired
    private KMAAPIShortTerm kmaAPIShortTerm;
    @Autowired
    private KMAApiUVIndex kmaApiUVIndex;


    private final RequestApiConfig requestApiConfig; //날씨 외부 API 호출을 위한 정보

    @Autowired
    public MeokuWeatherAPIServiceTest(RequestApiConfig requestApiConfig) {
        this.requestApiConfig = requestApiConfig;
    }

    @Test
    @DisplayName("날씨 데이터 가져오기")
    public void requestWeatherData() throws Exception {

        LocalDate weatherDateForSearch = LocalDate.now();
        WeatherDataDTO searchedWeatherDataDTO = meokuWeatherService.getWeatherDataFromDB(weatherDateForSearch);
        System.out.println(searchedWeatherDataDTO);
    }

    @Test
    @DisplayName("날씨 API 에러처리를 위한 테스트 - 1. 일부러 에러")
    public void requestAPIForErrorHandle(){
        // 현재 날짜 가져오기
        String todayDate = RequestApiUtil.getTodayDate();
        // 현재 시간 가져오기
        String hourOnlyTime = RequestApiUtil.getCurrentTime();
        //에러일때
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

        System.out.println(response.getBody());
        System.out.println(response.getHeaders().get("Content-Type"));
        System.out.println(response.getHeaders().getContentType());// 헤더가 무슨타입인지

        if(response.getHeaders().get("Content-Type").contains("text/xml;charset=UTF-8")){
            System.out.println("에러입니다");
        }

    }

    @Test
    @DisplayName("날씨 API 에러처리를 위한 테스트 - 2. 정상 작동")
    public void testse(){
        // 현재 날짜 가져오기
        String todayDate = RequestApiUtil.getTodayDate();
        // 현재 시간 가져오기
        String hourOnlyTime = RequestApiUtil.getCurrentTime();
        // 정상 작동일 때
        UriComponentsBuilder uriBuilder2 = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.ULTRA_SHORT_TERM_CURRENT_CONDITIONS_API_URL)
//                .queryParam("serviceKey", requestApiConfig.getWeatherApiEncodingKey())
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", todayDate)
                .queryParam("base_time", hourOnlyTime)
                .queryParam("nx", KMAApiConstants.MY_COMPANY_LOCATION_X)
                .queryParam("ny", KMAApiConstants.MY_COMPANY_LOCATION_Y);

        String uriString2 = uriBuilder2.toUriString();
        // 이미 인코딩된 serviceKey 추가 (UriComponentsBuilder 에서 인코딩 가능성이 있음)
        uriString2 += "&serviceKey=" + requestApiConfig.getWeatherApiEncodingKey();

        RestTemplate restTemplate2 = new RestTemplate();

        // serviceKey가 이미 인코딩 돼있기 때문에 추가 인코딩 방지
        URI uri = URI.create(uriString2);
        //API 호출!
        ResponseEntity<String> response2 = restTemplate2.getForEntity(uri, String.class);

        System.out.println(response2.getStatusCode());
        System.out.println(response2.getBody());
        System.out.println(response2.getHeaders().get("Content-Type"));

        if(response2.getHeaders().get("Content-Type").contains("text/xml;charset=UTF-8")){
            System.out.println("에러입니다");
        }
    }
    @Test
    @DisplayName("단기예보 API 테스트 - 안씀")
    public void reuqestShortTermForecast() throws Exception {
        WeatherDataDTO weatherDataDTO = meokuWeatherAPIService.getShortTermForecast();
        System.out.println(weatherDataDTO.toString());
    }

    @Test
    @DisplayName("자외선지수 API 테스트")
    public void requestUVIndex() throws Exception {
        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTimeToShort();

        WeatherDataDTO weatherDataDTO = kmaApiUVIndex.requestWeatherApi(requestDate, requestTime);
        System.out.println(weatherDataDTO);
    }

    @Test
    @DisplayName("스케쥴러에서 사용할 로직 그대로 사용해보기")
    public void test() throws Exception {
        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 현재 시간 가져오기
        String requestTime = RequestApiUtil.getCurrentTime();
        String requestTimeForShortTerm = "0200";
        LocalDate targetDate = LocalDate.now();

        WeatherDataDTO newWeatherDataDTO1 =  kmaAPIUltraShortTerm.requestWeatherApi(requestDate, requestTime);
        WeatherDataDTO newWeatherDataDTO2 =  kmaAPIShortTerm.requestWeatherApi(requestDate, requestTimeForShortTerm);

        WeatherData ultraShortTermResult =  meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO1);
        System.out.println(ultraShortTermResult.toString());
        WeatherData ShortTermResult =  meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO2);
        System.out.println(ShortTermResult.toString());

        //초단기 실황 데이터 저장
        WeatherData newUpdatedWeatherData = meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO1);
    }

    @Test
    @DisplayName("스케쥴러에서 사용할 로직 그대로 사용해보기")
    public void requestAndSaveUVIndex() throws Exception {
        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTimeToShort();
        LocalDate targetDate = LocalDate.now();

        WeatherDataDTO newWeatherDataDTO = kmaApiUVIndex.requestWeatherApi(requestDate, requestTime);
        //초단기 실황 데이터 저장
        WeatherData newUpdatedWeatherData = meokuWeatherService.updateWeatherDataFromApi(targetDate, newWeatherDataDTO);
    }
}
