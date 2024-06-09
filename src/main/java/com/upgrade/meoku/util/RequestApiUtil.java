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


public class RequestApiUtil {
    // 정적 메소드 사용을 위해
    private RequestApiUtil(){}

    // 기상철 API 호출을 위한 현재 날짜 가져오기
    public static String getTodayDate(){
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // 날짜를 %Y%m%d 형식으로 포맷팅하기
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
        String todayDate = currentDate.format(formatter1);

        return todayDate;
    }
    // 기상철 API 호출을 위한 현재 시간 가져오기 ex) 0500
    public static String getCurrentTime(){
        // 현재 (날짜) + 시간 가져오기
        LocalDateTime currentDateForTime = LocalDateTime.now();

        // 시간을 "HH" 형식의 문자열로 포맷
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH");
        String formattedTime = currentDateForTime.format(formatter2);

        // 시간을 시간단위로만 표현
        String hourOnlyTime = formattedTime + "00";

        return hourOnlyTime;
    }
}
