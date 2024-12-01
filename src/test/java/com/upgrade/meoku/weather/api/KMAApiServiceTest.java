package com.upgrade.meoku.weather.api;

import com.upgrade.meoku.util.RequestApiUtil;
import com.upgrade.meoku.weather.WeatherData;
import com.upgrade.meoku.weather.WeatherDataDTO;
import com.upgrade.meoku.weather.api.service.KMAAPIShortTerm;
import com.upgrade.meoku.weather.api.service.KMAAPIUltraShortTerm;
import com.upgrade.meoku.weather.api.service.KMAApiPerTemp;
import com.upgrade.meoku.weather.api.service.KMAApiUVIndex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KMAApiServiceTest {

    @Autowired
    KMAAPIUltraShortTerm kmaapiUltraShortTerm;
    @Autowired
    KMAAPIShortTerm kmaApiShortTermServices;
    @Autowired
    KMAApiUVIndex kmaApiUVIndex;
    @Autowired
    KMAApiPerTemp kmaApiPerTemp;

    @Test
    @DisplayName("위임 구조로 만든 초단기 실황 API 호출 테스트")
    public void KMAApiUltraShortTermTest() throws Exception {

        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 단기예보 호출을 위한 알맞은 시간 가져오기
        String requestTime = RequestApiUtil.getCurrentTime();

        WeatherDataDTO requestedWeatherDTO = kmaapiUltraShortTerm.requestWeatherApi(requestDate, requestTime);
        System.out.println(requestedWeatherDTO.toString());
    }


    @Test
    @DisplayName("위임 구조로 만든 단기 예보 API 호출 테스트")
    public void KMAApiShortTermTest() throws Exception {

        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        // 단기예보 호출을 위한 알맞은 시간 가져오기
        String requestTime = "0200";

        WeatherDataDTO requestedWeatherDTO = kmaApiShortTermServices.requestWeatherApi(requestDate, requestTime);
        System.out.println(requestedWeatherDTO.toString());

    }

    @Test
    @DisplayName("위임 구조로 만든 자외선지수 API 테스트")
    public void KMAApiUVIndexTest() throws Exception {
        String requestDate = RequestApiUtil.getTodayDate();
//        String requestTime = RequestApiUtil.getCurrentTimeToShort();
        String requestTime = "15";
        System.out.println(requestTime);
        WeatherDataDTO weatherDataDTO = kmaApiUVIndex.requestWeatherApi(requestDate, requestTime);
        if (weatherDataDTO != null) {
            System.out.println("weatherDataDTO");
        }else{
            System.out.println("일시적으로 자외선 지수를 가져올 수 없습니다. - API에서 데이터 제공 X");
        }
    }

    @Test
    @DisplayName("위임 구조로 만든 체감온도 API 호출 테스트")
    public void KMAApiPercivedTempTest() throws Exception {

        // 현재 날짜
        String requestDate = RequestApiUtil.getTodayDate();
        String requestTime = RequestApiUtil.getCurrentTimeToShort();

        WeatherDataDTO requestedWeatherDTO = kmaApiPerTemp.requestWeatherApi(requestDate, requestTime);
        if (requestedWeatherDTO == null) {
            System.out.println("요청 되지 않았어요!");
        }
        else{
            System.out.println(requestedWeatherDTO.toString());
        }

    }

}
