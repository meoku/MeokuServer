package com.upgrade.meoku.util;

public class MeokuUtil {

    // 인스턴스화 방지 : 보통의 Util 클래스의 함수는 정적 메서드로 구성 한다고함 -> 이유는 따로정리
    private MeokuUtil(){}

    //식단에서 특수문자, 공백 제거
    public static String removeCharacters(String input) {
        if (input == null) {
            return null;
        }
        // 한글과 '*'를 제외한 나머지 문자를 제거
        return input.replaceAll("[^가-힣0-9&*/]", "");

    }
}
