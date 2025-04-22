package com.upgrade.meoku.newmealmenu.util;

import com.upgrade.meoku.mealmenu.util.MenuSimilaritySearchUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class newMealMenuUtilTest {


    @Test
    @DisplayName("메뉴 유사도 테스트")
    public void testSimilarMenuSearch() {
        //이부분은 이제 dao로 교체 예정
        List<String> menuList = Arrays.asList("캔참치야채비빔밥", "참치김치찌개동원하지", "원점자캔참치야채비빔밥", "캔참치야채비빔밥동원정지", "김치볶음밥", "오므라이스");
        String input = "캔참치";

        List<String> result = MenuSimilaritySearchUtil.searchSimilarMenuSmart(input, menuList, 5);

        System.out.println(result);
//        assertTrue(result.contains("김치볶음밥"));
//        assertTrue(result.contains("참치김치찌개"));
//        assertFalse(result.contains("돈까스"));
    }

}
