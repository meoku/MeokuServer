package com.upgrade.meoku.weather.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.WeatherDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 단기 예보
public class KMAAPIShortTerm implements KMAApiService {

    @Override
    public WeatherDataDTO requestWeatherApi(String requestDate, String reqeustTime) throws Exception {
        // 현재 날짜 가져오기
//        String requestDate = RequestApiUtil.getTodayDate();
//        // 현재 시간 가져오기
//        String hourOnlyTime = "2000"; //최고기온은 15시, 최저기온은 06시부터 이기 때문에

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KMAApiConstants.SHORT_TERM_FORECAST_API_URL)
                .queryParam("serviceKey", KMAApiConstants.weatherApiEncodingKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 300) //하루 예보는 이론상 최대 96개
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

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            Map<String, Object> responseContent = (Map<String, Object>) responseMap.get("response");
            Map<String, Object> body = (Map<String, Object>) responseContent.get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

            // item -> KMAApiShortTermResponseDTO 변환 and 요청한 날짜의 예보정보만 가져오기 (baseDate = fcstDate)
            List<KMAApiShortTermResponseDTO> weatherForecastList = itemList.stream()
                    .map(item -> objectMapper.convertValue(item, KMAApiShortTermResponseDTO.class))
                    .filter(dto -> dto.getFcstDate().equals(requestDate)) // 여기에 원하는 baseDate 값을 넣어주세요
                    .collect(Collectors.toList());

            // 최대기온, 최소기온 가져오기
            String tmxValue = weatherForecastList.stream()
                    .filter(dto -> "TMX".equals(dto.getCategory()))
                    .findFirst()
                    .map(KMAApiShortTermResponseDTO::getFcstValue)
                    .orElse(null);
            String tmnValue = weatherForecastList.stream()
                    .filter(dto -> "TMN".equals(dto.getCategory()))
                    .findFirst()
                    .map(KMAApiShortTermResponseDTO::getFcstValue)
                    .orElse(null);

            // 요청한 시간대의 데이터 가져오기 (요청 한시간 뒤 정보부터 시작함)
            LocalTime time = LocalTime.parse(reqeustTime, DateTimeFormatter.ofPattern("HHmm")); // 문자열을 LocalTime 객체로 파싱
            LocalTime newTime = time.plusHours(1);                                  // 1시간 추가
            String targetNewTime = newTime.format(DateTimeFormatter.ofPattern("HHmm"));         // 새로운 시간을 문자열로 변환하여 반환

            List<KMAApiShortTermResponseDTO> filteredList = weatherForecastList.stream()        //요청시간 +1 시간대의 단기예보 정보 List
                    .filter(dto -> dto.getFcstTime().equals(targetNewTime))
                    .collect(Collectors.toList());

            WeatherDataDTO meokuWeather = RequestApiUtil.APIResponseToWeatherDataDTOForShortTermAPI(filteredList);
            meokuWeather.setDailyMaximumTemperature(tmxValue);
            meokuWeather.setDailyMinimumTemperature(tmnValue);

            return meokuWeather;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}