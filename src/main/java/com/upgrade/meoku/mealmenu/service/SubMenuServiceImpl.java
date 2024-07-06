package com.upgrade.meoku.mealmenu.service;

import com.upgrade.meoku.config.NaverCloudConfig;
import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.dto.MeokuDetailedMenuDTO;
import com.upgrade.meoku.mealmenu.data.dao.SubMenuDao;
import com.upgrade.meoku.mealmenu.data.dto.SubDailyMenuDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsDTO;
import com.upgrade.meoku.mealmenu.data.dto.SubMenuDetailsItemBridgeDTO;
import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetails;
import com.upgrade.meoku.util.MeokuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SubMenuServiceImpl implements SubMenuService{
    private final RestTemplate restTemplate;        //NAVER 외부 API 호출을 위한 객체
    private final NaverCloudConfig naverCloudConfig; //NAVER 외부 API 호출을 위한 정보
    private final SubMenuDao subMenuDao;        // 메뉴 관련 Dao
    @Autowired
    public SubMenuServiceImpl(RestTemplate restTemplate,
                              NaverCloudConfig naverCloudConfig,
                              SubMenuDao subMenuDao){
        this.restTemplate = restTemplate;
        this.naverCloudConfig = naverCloudConfig;
        this.subMenuDao = subMenuDao;
    }

    @Override
    public List<SubDailyMenuDTO> MenuImageUploadAndReturnMenuData(MultipartFile menuFile) throws Exception {

        //이미지 없으면 에러
        if (menuFile.isEmpty()) throw new Exception();

        // 이미지 파일의 확장자 확인
        String fileName = menuFile.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.equals("jpg") && !fileExtension.equals("png") && !fileExtension.equals("pdf")) {
            throw new Exception("지원하지 않는 파일 형식입니다.");
        }

        // 요청 JSON 데이터 구성
        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("requestId", UUID.randomUUID().toString());
        requestJson.put("version", "V1");
        requestJson.put("timestamp", System.currentTimeMillis());

        // 이미지 파일을 바이트 배열로 읽어오기
        byte[] imageBytes = menuFile.getBytes();
        // 이미지 파일을 base64로 인코딩
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> imageInfo = new HashMap<>();
        imageInfo.put("data", base64Image);
        imageInfo.put("format", fileExtension);
        imageInfo.put("name", "demo");
        imageInfo.put("templateIds", new Integer[]{naverCloudConfig.getTEMPLATEIDS()});

        requestJson.put("images", new Map[]{imageInfo});

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", naverCloudConfig.getNaverApiScretKey());

        // 요청 엔티티 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestJson, headers);

        // POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(naverCloudConfig.getNaverOcrUrl(), requestEntity, Map.class);
        Map<String, Object> responseObject = responseEntity.getBody();

        return this.getDailyMenuDTOList(responseObject);
    }

    private List<SubDailyMenuDTO> getDailyMenuDTOList(Map<String, Object> map) throws Exception {
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

                    curDailyMenuDTO.setMenuDate(this.getDate(menuArray[0]));//첫번째는 날짜이므로 Date로 변환한 값 저장

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
    private Timestamp getDate(String dateString) throws ParseException {
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

}
