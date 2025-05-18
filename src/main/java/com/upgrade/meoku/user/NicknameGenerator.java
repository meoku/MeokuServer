package com.upgrade.meoku.user;

import java.util.List;
import java.util.Random;

public class NicknameGenerator {
    private static final List<String> ADJECTIVES = List.of(
            "상큼한", "고소한", "든든한", "촉촉한", "바삭한", "부드러운", "건강한", "향긋한", "쫄깃한", "시원한",
            "따뜻한", "담백한", "진한", "은은한", "화끈한", "짭짤한", "깔끔한", "싱그러운", "기분좋은", "정갈한",
            "향기로운", "매콤한", "훈훈한", "새콤한", "귀여운", "어여쁜", "영양만점", "가뿐한", "풍성한", "자극적인",
            "달큰한", "바람직한", "스며드는", "포근한", "노곤한", "따사로운", "깨끗한", "푹신한", "상쾌한", "화사한",
            "말랑한", "가성비좋은", "신선한", "톡쏘는", "피톤치드", "유기농", "하늘빛", "빛나는", "한입가득", "정성가득"
    );

    private static final List<String> FOODS = List.of(
            "현미밥", "귀리", "보리밥", "양배추", "시금치", "두부", "닭가슴살", "계란찜", "단호박", "토마토",
            "브로콜리", "고구마", "아보카도", "샐러드", "양상추", "참치", "연어", "버섯", "콩나물", "김치",
            "나물", "미역국", "된장찌개", "채소볶음", "무말랭이", "청경채", "고추장불고기", "콩비지", "쌈채소", "쌈장",
            "닭죽", "계란말이", "치즈오믈렛", "잡곡밥", "유부초밥", "애호박", "감자", "배추", "순두부", "불고기",
            "현미누룽지", "오이", "무쌈", "해초", "도라지", "김밥", "곤약", "양파", "파프리카", "마늘"
    );

    private static final List<String> ANIMALS = List.of(
            "고양이", "강아지", "토끼", "여우", "곰", "펭귄", "사자", "호랑이", "햄스터", "다람쥐",
            "부엉이", "너구리", "고슴도치", "수달", "늑대", "얼룩말", "기린", "코끼리", "판다", "앵무새",
            "거북이", "돌고래", "참새", "까치", "청설모", "도마뱀", "까마귀", "삵", "족제비", "하이에나",
            "염소", "양", "코알라", "두더지", "돼지", "닭", "칠면조", "까투리", "오리", "고라니",
            "매", "독수리", "문어", "오징어", "게", "가재", "낙타", "멧돼지", "바다표범", "비둘기"
    );

    private static final List<String> HEALTH = List.of(
            "저염", "고단백", "로우카브", "비건", "클린", "헬시", "식단", "다이어트", "해독", "그린",
            "무설탕", "탄단지", "유산균", "장건강", "저탄수", "무가당", "해독쥬스", "올리브", "식이섬유", "영양소",
            "균형잡힌", "항산화", "무첨가", "가벼운", "속편한", "디톡스", "건강미", "고섬유", "프로틴", "웰빙"
    );

    private static final List<String> COLORS = List.of(
            "하얀", "검정", "회색", "노랑", "주황", "초록", "파랑", "남색", "보라", "갈색",
            "연두", "살구", "하늘", "연보라", "크림"
    );

    private static final List<String> TRAITS = List.of(
            "지킴이", "마스터", "요정", "도사", "귀신", "러버", "왕자", "장인", "매니아", "수호자",
            "알파", "감별사", "개척자", "헌터", "먹보", "탐험가", "전도사", "통찰자", "비평가", "디자이너",
            "세프", "파일럿", "감시자", "서포터", "영웅", "기획자", "스페셜리스트", "전문가", "대표", "천재"
    );

    private static final Random RANDOM = new Random();

    public static String generate() {
        int pattern = RANDOM.nextInt(7);

        switch (pattern) {
            case 0: return pick(ADJECTIVES) + pick(FOODS);
            case 1: return pick(FOODS) + pick(ANIMALS);
            case 2: return pick(ADJECTIVES) + pick(FOODS) + pick(ANIMALS);
            case 3: return pick(ADJECTIVES) + pick(HEALTH) + pick(FOODS);
            case 4: return pick(FOODS) + pick(TRAITS);
            case 5: return pick(ADJECTIVES) + pick(COLORS) + pick(FOODS);
            case 6: return pick(HEALTH) + pick(FOODS) + pick(ANIMALS);
            default: return pick(ADJECTIVES) + pick(FOODS);
        }
    }

    private static String pick(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
