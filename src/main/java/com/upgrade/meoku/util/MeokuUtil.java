package com.upgrade.meoku.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MeokuUtil {

    // 인스턴스화 방지 : 보통의 Util 클래스의 함수는 정적 메서드로 구성 한다고함 -> 이유는 따로정리
    private MeokuUtil(){}

    // 해당날의 주간 days(시작, 끝) 구하기
    public static List<LocalDate> getWeekdaysInWeek(LocalDate date) {
        List<LocalDate> weekdays = new ArrayList<>();

        // 해당 주의 첫 날을 구하기
        LocalDate firstDayOfWeek = date.with(DayOfWeek.MONDAY);

        // 해당 주의 평일을 구하기
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = firstDayOfWeek.plusDays(i);
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                weekdays.add(currentDate);
            }
        }

        return weekdays;
    }

    //식단에서 특수문자, 공백 제거
    public static String removeCharacters(String input) {
        if (input == null) {
            return null;
        }
        // 한글, 숫자 & * /'를 제외한 나머지 문자를 제거
        return input.replaceAll("[^가-힣0-9&*/]", "");

    }
}
