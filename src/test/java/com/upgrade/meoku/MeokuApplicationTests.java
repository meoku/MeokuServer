package com.upgrade.meoku;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@SpringBootTest
class MeokuApplicationTests {

	@Test
	void contextLoads() {
		String dateString = "9월3일 (목)";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MMMMd일 (E)", Locale.KOREAN);
		try {
			// 올해의 년도 가져오기
			int currentYear = LocalDate.now().getYear();

			// 입력 문자열에서 년도를 현재 년도로 변경하여 처리
			dateString = currentYear + "년 " + dateString;

			Date date = dateFormat.parse(dateString);
			Timestamp timestamp = new Timestamp(date.getTime());
			System.out.println(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
