package com.upgrade.meoku.mealmenu.util;

import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsItemBridgeDTO;
import com.upgrade.meoku.util.MeokuUtil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuUtil {
    // 정적 메소드 사용을 위해
    private MenuUtil(){}

    public static List<SubDailyMenuDTO> getDailyMenuDTOList(Map<String, Object> map) throws Exception {
        // responseObject에서 fields 리스트를 가져옴
        List<Map<String, Object>> images = (List<Map<String, Object>>) map.get("images");
        List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) images.get(0).get("fields");

        //주간 식당 저장할 DTO 리스트(5일이니 총 5개 객체를 담은 배열)
        List<SubDailyMenuDTO> returnList = Stream.generate(SubDailyMenuDTO::new)
                .limit(5)
                .collect(Collectors.toList());

        // fields 리스트가 비어있지 않은지 확인
        if (fieldsList != null && !fieldsList.isEmpty()) {
            int idx = 0;
            // fields 리스트에 있는 각 맵에서 inferText 값을 추출
            for (Map<String, Object> field : fieldsList) {

                String menuType = (String) field.get("name");
                String[] menuTypeParts = menuType.split("_");
                //ex)monday_lunch 형식이 아니면 에러
                if (menuTypeParts.length != 2) throw new Exception("메뉴 타입이 이상합니다");

                String inferText = (String) field.get("inferText");//메뉴목록 get
                String[] menuArray = inferText.split("\\r?\\n");//줄바꿈으로 구분되어있는 메뉴들 to 배열
                int menuArraySize = menuArray.length;

                //점심,저녁 * 5일 해서 총 10개의 데이터가 들어오는데 주간 데이터는 5개이므로 아래와 같이 인덱싱
                SubDailyMenuDTO curDailyMenuDTO = returnList.get(idx/2);

                //점심, 저녁 데이터가 아직 생성 안됐다면 생성
                if(curDailyMenuDTO.getMenuDetailsList() == null ||
                        curDailyMenuDTO.getMenuDetailsList().isEmpty()) {
                    curDailyMenuDTO.setMenuDetailsList(new ArrayList<SubMenuDetailsDTO>());
                }

                if (menuTypeParts[1].trim().equalsIgnoreCase("lunch")) {    //점심메뉴 파싱

                    curDailyMenuDTO.setMenuDate(MenuUtil.getDate(menuArray[0]));//첫번째는 날짜이므로 Date로 변환한 값 저장

                    //OCR로 나온 메뉴에서 특수문자 및 영어 제거 By MeokuUtil
                    menuArray = Arrays.stream(menuArray)
                            .map(MeokuUtil::removeCharacters)
                            .toArray(String[]::new);

                    // 총 메뉴 길이가 최소 13개이상 일 때 메뉴정보 넣기
                    if(menuArraySize > 13){
                        curDailyMenuDTO.setHolidayFg("N");
                        curDailyMenuDTO.setRestaurantOpenFg("Y");
                        //한식 (6개)
                        SubMenuDetailsDTO koreaMenuDetailsDTO = new SubMenuDetailsDTO();
                        koreaMenuDetailsDTO.setMenuDetailsName("한식");
                        koreaMenuDetailsDTO.setMainMealYn("Y");
                        for(int i = 1; i <= 6 ; i++){
                            SubMenuDetailsItemBridgeDTO bridgeDTO = new SubMenuDetailsItemBridgeDTO();
                            bridgeDTO.setMenuItemName(menuArray[i]);
                            if(i == 1) bridgeDTO.setMainMenuYn("Y");
                            else bridgeDTO.setMainMenuYn("N");
                            koreaMenuDetailsDTO.getSubBridgeList().add(bridgeDTO);
                        }
                        curDailyMenuDTO.getMenuDetailsList().add(koreaMenuDetailsDTO); //한식 넣기

                        //일품 (5개씩)
                        SubMenuDetailsDTO oneMenuDetailsDTO = new SubMenuDetailsDTO();
                        oneMenuDetailsDTO.setMenuDetailsName("일품");
                        oneMenuDetailsDTO.setMainMealYn("Y");
                        for(int i = 7; i <= 11 ; i++){
                            SubMenuDetailsItemBridgeDTO bridgeDTO = new SubMenuDetailsItemBridgeDTO();
                            bridgeDTO.setMenuItemName(menuArray[i]);
                            if(i == 7) bridgeDTO.setMainMenuYn("Y");
                            else bridgeDTO.setMainMenuYn("N");
                            oneMenuDetailsDTO.getSubBridgeList().add(bridgeDTO);
                        }
                        curDailyMenuDTO.getMenuDetailsList().add(oneMenuDetailsDTO); //일품 넣기

                        //Plus (뒤에서 3번째, 2번째)
                        SubMenuDetailsDTO plusMenuDetailsDTO = new SubMenuDetailsDTO();
                        plusMenuDetailsDTO.setMenuDetailsName("Plus");
                        plusMenuDetailsDTO.setMainMealYn("N");
                        SubMenuDetailsItemBridgeDTO plusBridgeDTO1 = new SubMenuDetailsItemBridgeDTO();
                        SubMenuDetailsItemBridgeDTO plusBridgeDTO2 = new SubMenuDetailsItemBridgeDTO();

                        plusBridgeDTO1.setMenuItemName(menuArray[menuArraySize-3]);
                        plusBridgeDTO1.setMainMenuYn("N");
                        plusBridgeDTO2.setMenuItemName(menuArray[menuArraySize-2]);
                        plusBridgeDTO2.setMainMenuYn("N");

                        plusMenuDetailsDTO.getSubBridgeList().add(plusBridgeDTO1);
                        plusMenuDetailsDTO.getSubBridgeList().add(plusBridgeDTO2);

                        curDailyMenuDTO.getMenuDetailsList().add(plusMenuDetailsDTO); //Plus 넣기

                        //샐러드팩 (뒤에서 첫번째)
                        SubMenuDetailsDTO saladMenuDetailsDTO = new SubMenuDetailsDTO();
                        saladMenuDetailsDTO.setMenuDetailsName("샐러드팩");
                        saladMenuDetailsDTO.setMainMealYn("N");

                        SubMenuDetailsItemBridgeDTO saladBridgeDTO = new SubMenuDetailsItemBridgeDTO();
                        saladBridgeDTO.setMenuItemName(menuArray[menuArraySize-1]);
                        saladBridgeDTO.setMainMenuYn("N");

                        saladMenuDetailsDTO.getSubBridgeList().add(saladBridgeDTO);

                        curDailyMenuDTO.getMenuDetailsList().add(saladMenuDetailsDTO); //샐러드 넣기

                    }else{// 5개 이하일 때는 식당 오픈 X
                        curDailyMenuDTO.setHolidayFg("Y");
                        curDailyMenuDTO.setRestaurantOpenFg("N");
                    }
                }else if(menuTypeParts[1].trim().equalsIgnoreCase("diner")) {//저녁메뉴 파싱
                    if(menuArraySize >= 6){
                        //OCR로 나온 메뉴에서 특수문자 및 영어 제거 By MeokuUtil
                        menuArray = Arrays.stream(menuArray)
                                .map(MeokuUtil::removeCharacters)
                                .toArray(String[]::new);

                        //석식 (6개씩)
                        SubMenuDetailsDTO dinerMenuDetailsDTO = new SubMenuDetailsDTO();
                        dinerMenuDetailsDTO.setMenuDetailsName("석식");
                        dinerMenuDetailsDTO.setMainMealYn("Y");
                        for(int i = 0; i <= 4 ; i++){
                            SubMenuDetailsItemBridgeDTO bridgeDTO = new SubMenuDetailsItemBridgeDTO();
                            bridgeDTO.setMenuItemName(menuArray[i]);
                            if(i == 0) bridgeDTO.setMainMenuYn("Y");
                            else bridgeDTO.setMainMenuYn("N");
                            dinerMenuDetailsDTO.getSubBridgeList().add(bridgeDTO);
                        }
                        curDailyMenuDTO.getMenuDetailsList().add(dinerMenuDetailsDTO); //석식 넣기

                        //Plus 뒤에 두개
                        SubMenuDetailsDTO plusMenuDetailsDTO = new SubMenuDetailsDTO();
                        plusMenuDetailsDTO.setMenuDetailsName("Plus");
                        plusMenuDetailsDTO.setMainMealYn("N");
                        SubMenuDetailsItemBridgeDTO plusBridgeDTO1 = new SubMenuDetailsItemBridgeDTO();
                        SubMenuDetailsItemBridgeDTO plusBridgeDTO2 = new SubMenuDetailsItemBridgeDTO();

                        plusBridgeDTO1.setMenuItemName(menuArray[menuArraySize-2]);
                        plusBridgeDTO1.setMainMenuYn("N");
                        plusBridgeDTO2.setMenuItemName(menuArray[menuArraySize-1]);
                        plusBridgeDTO2.setMainMenuYn("N");

                        plusMenuDetailsDTO.getSubBridgeList().add(plusBridgeDTO1);
                        plusMenuDetailsDTO.getSubBridgeList().add(plusBridgeDTO2);

                        curDailyMenuDTO.getMenuDetailsList().add(plusMenuDetailsDTO); //Plus 넣기
                    }
                }

                idx++;
            }
        } else {
            // fields 리스트가 비어있을 때 처리할 내용을 작성합니다.
            System.out.println("No fields found.");
        }
        return returnList;
    }
    // ex) 3월3일 (일) 이라는 값을 Timestamp로 변환 (년도는 현재기준 올해 년도)
    public static Timestamp getDate(String dateString) throws ParseException {
        // "(요일)" 부분 제거
        dateString = dateString.replaceAll("\\(.*\\)", "");

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);
        try {
            // 올해의 년도 가져오기
            int currentYear = LocalDate.now().getYear();
            // 입력 문자열에서 년도를 현재 년도로 변경하여 처리
            dateString = currentYear + "년 " + dateString;
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }
    // N일 뒤 날짜 계산
    public static Timestamp getTimestampAfterNdays(LocalDate startDate, int Nday) {
        // N일 뒤의 날짜 계산하기
        LocalDate dateAfterNDays = startDate.plusDays(Nday);
        // LocalDate를 LocalDateTime으로 변환하고 시간을 00:00:00으로 설정
        LocalDateTime dateTimeAfter15Days = dateAfterNDays.atStartOfDay();
        // LocalDateTime을 Timestamp로 변환하기
        Timestamp timestampAfterNday = Timestamp.valueOf(dateTimeAfter15Days);
        return timestampAfterNday;
    }
    //지금 현재 날짜 가져오기
    public static Timestamp getCurrentTimestamp() {
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 원하는 형식으로 날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        // 문자열을 Timestamp로 변환
        Timestamp curTimestamp = Timestamp.valueOf(formattedDate + " 00:00:00");
        return curTimestamp;
    }

}
