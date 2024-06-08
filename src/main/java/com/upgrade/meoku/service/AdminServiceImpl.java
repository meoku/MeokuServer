package com.upgrade.meoku.service;

import com.upgrade.meoku.config.NaverCloudConfig;
import com.upgrade.meoku.data.dao.MeokuMenuDao;
import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import com.upgrade.meoku.data.dto.MeokuDetailedMenuDTO;
import com.upgrade.meoku.data.entity.MeokuDailyMenu;
import com.upgrade.meoku.data.entity.MeokuDetailedMenu;
import com.upgrade.meoku.data.entity.MeokuMenuDetail;
import com.upgrade.meoku.util.MeokuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class AdminServiceImpl implements AdminService{

    private final RestTemplate restTemplate;        //NAVER 외부 API 호출을 위한 객체
    private final NaverCloudConfig naverCloudConfig; //NAVER 외부 API 호출을 위한 정보
    private final MeokuMenuDao meokuMenuDao;        // 메뉴 관련 Dao
    @Autowired
    public AdminServiceImpl(RestTemplate restTemplate,
                            NaverCloudConfig naverCloudConfig,
                            MeokuMenuDao meokuMenuDao) {
        this.restTemplate = restTemplate;
        this.naverCloudConfig = naverCloudConfig;
        this.meokuMenuDao = meokuMenuDao;
    }

    @Override
    public List<MeokuDailyMenuDTO> MenuImageUploadAndReturnMenuData(MultipartFile menuFile) throws Exception {

        if (menuFile.isEmpty()) { //이미지 없으면 에러
            //에러 발생
            throw new Exception();
        }
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

        return this.getMeokuDailyMenuDTOList(responseObject);
    }
    @Override
    @Transactional
    public void WeekMenuUpload(List<MeokuDailyMenuDTO> weekMenu){
        //List<MeokuDailyMenuDTO> 각각 객체 List로 가져오기
        List<MeokuDailyMenu> savedDailyMenuList = new ArrayList<>();

        //DailyMenu
        for(MeokuDailyMenuDTO dailyMenuDTO : weekMenu){
            MeokuDailyMenu savedDailyMenu = new MeokuDailyMenu();
            savedDailyMenu.setDate(dailyMenuDTO.getDate());
            savedDailyMenu.setHolidayFg(dailyMenuDTO.getHolidayFg());
            savedDailyMenu.setRestaurantOpenFg(dailyMenuDTO.getRestaurantOpenFg());

            //해당 날짜로 이미 존재하는 데이터가 있다면 취소처리 필요

            //DailyMenu Data 저장
            meokuMenuDao.insertDailyMenu(savedDailyMenu);

            //저장될 상세식단 List
            List<MeokuDetailedMenu> savedDetailedMenuList = new ArrayList<>();

            //상세 식단
            //param(only menuName)
            for(MeokuDetailedMenuDTO detailedMenuDTO : dailyMenuDTO.getDetailedMenuDTOList()){
                MeokuDetailedMenu savedDetailedMenu = new MeokuDetailedMenu();
                savedDetailedMenu.setMeokuDailyMenu(savedDailyMenu);
                savedDetailedMenu.setDailyMenuDate(detailedMenuDTO.getDailyMenuDate());
                savedDetailedMenu.setDailyMenuCategory(detailedMenuDTO.getDailyMenuCategory());
                savedDetailedMenu.setMainMenuYn(detailedMenuDTO.getMainMenuYn());
                savedDetailedMenu.setDetailedMenuName(detailedMenuDTO.getDetailedMenuName());

                //메뉴 이름으로 MenuDetail에서 찾은 후 없으면 데이터 저장( 저장은 Dao에서 처리)
                MeokuMenuDetail mainMenuEntity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMainMenuName());
                MeokuMenuDetail menu1Entity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMenu1Name());
                MeokuMenuDetail menu2Entity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMenu2Name());
                MeokuMenuDetail menu3Entity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMenu3Name());
                MeokuMenuDetail menu4Entity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMenu4Name());
                MeokuMenuDetail menu5Entity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMenu5Name());
                MeokuMenuDetail menu6Entity = meokuMenuDao.searchMenuDetailAndSave(detailedMenuDTO.getMenu6Name());

                //상세식단에 메뉴정보 넣기
                savedDetailedMenu.setMainMenu(mainMenuEntity);
                savedDetailedMenu.setMenu1(menu1Entity);
                savedDetailedMenu.setMenu2(menu2Entity);
                savedDetailedMenu.setMenu3(menu3Entity);
                savedDetailedMenu.setMenu4(menu4Entity);
                savedDetailedMenu.setMenu5(menu5Entity);
                savedDetailedMenu.setMenu6(menu6Entity);

                savedDetailedMenuList.add(savedDetailedMenu);
            }

            //DetailedMenu Data 저장
            meokuMenuDao.insertDetailedMenu(savedDetailedMenuList);
        }
    }

    private List<MeokuDailyMenuDTO> getMeokuDailyMenuDTOList(Map<String, Object> map) throws Exception {
        // responseObject에서 fields 리스트를 가져옴
        List<Map<String, Object>> images = (List<Map<String, Object>>) map.get("images");
        List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) images.get(0).get("fields");

        //주간 식당 저장할 DTO 리스트(5일이니 총 5개 객체를 담은 배열)
        List<MeokuDailyMenuDTO> returnList = Stream.generate(MeokuDailyMenuDTO::new)
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
                if(menuTypeParts.length != 2) throw new Exception("메뉴 타입이 이상합니다");

                String inferText = (String) field.get("inferText");//메뉴목록 get
                String[] menuArray = inferText.split("\\r?\\n");//줄바꿈으로 구분되어있는 메뉴들 to 배열
                int menuArraySize = menuArray.length;

                //점심,저녁 * 5일 해서 총 10개의 데이터가 들어오는데 주간 데이터는 5개이므로 아래와 같이 인덱싱
                MeokuDailyMenuDTO curMeokuDailyMenuDTO = returnList.get(idx/2);

                //점심, 저녁 데이터가 아직 생성 안됐다면 생성
                if(curMeokuDailyMenuDTO.getDetailedMenuDTOList() == null ||
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().isEmpty()) {
                    curMeokuDailyMenuDTO.setDetailedMenuDTOList(new ArrayList<MeokuDetailedMenuDTO>());
                }

                if (menuTypeParts[1].trim().equalsIgnoreCase("lunch")) {    //점심메뉴
                    curMeokuDailyMenuDTO.setDate(this.getDate(menuArray[0]));//첫번째는 날짜이므로 Date로 변환한 값 저장

                    //OCR로 나온 메뉴에서 특수문자 및 영어 제거 By MeokuUtil
                    menuArray = Arrays.stream(menuArray)
                            .map(MeokuUtil::removeCharacters)
                            .toArray(String[]::new);

                    // 총 메뉴 길이가 최소 13개이상 일 때 메뉴정보 넣기
                    if(menuArraySize > 13){
                        curMeokuDailyMenuDTO.setHolidayFg("N");
                        curMeokuDailyMenuDTO.setRestaurantOpenFg("Y");
                        //한식 (6개씩)
                        MeokuDetailedMenuDTO menu1 = new MeokuDetailedMenuDTO();
                        menu1.setDetailedMenuName("한식");
                        menu1.setMainMenuYn("Y");
                        menu1.setMainMenuName(menuArray[1]);
                        menu1.setMenu1Name(menuArray[2]);
                        menu1.setMenu2Name(menuArray[3]);
                        menu1.setMenu3Name(menuArray[4]);
                        menu1.setMenu4Name(menuArray[5]);
                        menu1.setMenu5Name(menuArray[6]);
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().add(menu1);
                        //일품 (5개씩)
                        MeokuDetailedMenuDTO menu2 = new MeokuDetailedMenuDTO();
                        menu2.setDetailedMenuName("일품");
                        menu2.setMainMenuYn("Y");
                        menu2.setMainMenuName(menuArray[7]);
                        menu2.setMenu1Name(menuArray[8]);
                        menu2.setMenu2Name(menuArray[9]);
                        menu2.setMenu3Name(menuArray[10]);
                        menu2.setMenu4Name(menuArray[11]);
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().add(menu2);
                        //Plus (뒤에서 3번째, 2번째)
                        MeokuDetailedMenuDTO menu3 = new MeokuDetailedMenuDTO();
                        menu3.setDetailedMenuName("점심Plus");
                        menu3.setMainMenuYn("N");
                        menu3.setMenu1Name(menuArray[menuArraySize-3]);
                        menu3.setMenu2Name(menuArray[menuArraySize-2]);
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().add(menu3);
                        //샐러드팩 (뒤에서 첫번째)
                        MeokuDetailedMenuDTO menu4 = new MeokuDetailedMenuDTO();
                        menu4.setDetailedMenuName("샐러드팩");
                        menu4.setMainMenuYn("N");
                        menu4.setMainMenuName(menuArray[menuArraySize-1]);
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().add(menu4);

                    }else{// 5개 이하일 때는 식당 오픈 X
                        curMeokuDailyMenuDTO.setHolidayFg("Y");
                        curMeokuDailyMenuDTO.setRestaurantOpenFg("N");
                    }

                }else if(menuTypeParts[1].trim().equalsIgnoreCase("diner")){//저녁메뉴
                    if(menuArraySize >= 6){

                        //OCR로 나온 메뉴에서 특수문자 및 영어 제거 By MeokuUtil
                        menuArray = Arrays.stream(menuArray)
                                .map(MeokuUtil::removeCharacters)
                                .toArray(String[]::new);

                        //석식 (6개씩)
                        MeokuDetailedMenuDTO dinerMenu = new MeokuDetailedMenuDTO();
                        dinerMenu.setDetailedMenuName("석식");
                        dinerMenu.setMainMenuYn("Y");
                        dinerMenu.setMainMenuName(menuArray[0]);
                        dinerMenu.setMenu1Name(menuArray[1]);
                        dinerMenu.setMenu2Name(menuArray[2]);
                        dinerMenu.setMenu3Name(menuArray[3]);
                        dinerMenu.setMenu4Name(menuArray[4]);
                        //dinerMenu.setMenu5Name(menuArray[5]);
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().add(dinerMenu);
                        //Plus 뒤에 두개
                        MeokuDetailedMenuDTO dinerPlus = new MeokuDetailedMenuDTO();
                        dinerPlus.setDetailedMenuName("석식Plus");
                        dinerPlus.setMainMenuYn("N");
                        dinerPlus.setMenu1Name(menuArray[menuArraySize-2]);
                        dinerPlus.setMenu2Name(menuArray[menuArraySize-1]);
                        curMeokuDailyMenuDTO.getDetailedMenuDTOList().add(dinerPlus);
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
