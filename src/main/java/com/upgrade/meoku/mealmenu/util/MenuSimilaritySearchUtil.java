package com.upgrade.meoku.mealmenu.util;

import com.upgrade.meoku.mealmenu.data.dto.SubMenuItemDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuItem;

import java.util.*;
import java.util.stream.Collectors;

public class MenuSimilaritySearchUtil {

    // Levenshtein Distance 계산 함수
    public static int levenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }

        return dp[a.length()][b.length()];
    }

    // 유사한 메뉴 리스트 찾기 (단순 최대 거리 기준)
    public static List<String> searchSimilarMenus(String input, List<String> menuList, int maxDistance) {
        List<String> result = new ArrayList<>();
        for (String menu : menuList) {
            int distance = levenshtein(input, menu);
            if (distance <= maxDistance) {
                result.add(menu);
            }
        }
        return result;
    }
    // 가중치를 추가 적용한 유사 메뉴리스트 찾기
    public static List<String> searchSimilarMenuSmart(String input, List<String> menuList, int topN) {
        final String lowerInput = input.toLowerCase();

        // 거리, 가중치 등 사전 계산
        List<MenuScore> scoredList = new ArrayList<>();
        for (String menu : menuList) {
            String lowerMenu = menu.toLowerCase();
            int score = 0;

            //처음 시작이 입력값으로 시작하거나, 입력값 포함인 경우 가중치 대폭 상승시작
            if (lowerMenu.startsWith(lowerInput)) score += 100;
            else if (lowerMenu.contains(lowerInput)) score += 50;
            // 거리 재기
            int distance = levenshtein(lowerInput, lowerMenu);
            score -= distance;

            scoredList.add(new MenuScore(menu, score));
        }

        // 정렬: O(N log N), 거리 계산은 O(N) => 총 NlogN + N * M^2(문자열)
        scoredList.sort((a, b) -> Integer.compare(b.score, a.score));

        return scoredList.stream()
                .limit(topN)
                .map(ms -> ms.menu)
                .collect(Collectors.toList());
    }

    // 가중치를 추가 적용한 유사 메뉴리스트 찾기 - SubMenuItemDTO 버전
    public static List<SubMenuItemDTO> searchSimilarMenuSmartForMenuItemDTO(String input, List<SubMenuItemDTO> menuItemDTOList, int topN) {
        // 거리, 가중치 등 사전 계산
        List<MenuScore> scoredList = new ArrayList<>();
        for (SubMenuItemDTO dto : menuItemDTOList) {
            int score = 0;

            //처음 시작이 입력값으로 시작하거나, 입력값 포함인 경우 가중치 대폭 상승시작
            if (dto.getMenuItemName().startsWith(input)) score += 100;
            else if (dto.getMenuItemName().contains(input)) score += 50;
            // 거리 재기
            int distance = levenshtein(input, dto.getMenuItemName());
            score -= distance;

            scoredList.add(new MenuScore(dto, score));
        }

        // 정렬: O(N log N), 거리 계산은 O(N) => 총 NlogN + N * M^2(문자열)
        scoredList.sort((a, b) -> Integer.compare(b.score, a.score));

        return scoredList.stream()
                .limit(topN)
                .map(ms -> ms.dto)
                .collect(Collectors.toList());
    }

    static class MenuScore {
        String menu;
        SubMenuItemDTO dto;
        int score;
        //문자열만 사용
        MenuScore(String menu, int score) {
            this.menu = menu;
            this.score = score;
        }
        //menuItemDTO전용
        MenuScore(SubMenuItemDTO dto, int score) {
            this.dto = dto;
            this.score = score;
        }
    }
}
