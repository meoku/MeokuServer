package com.upgrade.meoku.mealmenu.service;

import com.upgrade.meoku.config.NaverCloudConfig;
import com.upgrade.meoku.mealmenu.data.dao.SubMenuDao;
import com.upgrade.meoku.mealmenu.data.dto.*;
import com.upgrade.meoku.mealmenu.data.entity.*;
import com.upgrade.meoku.mealmenu.util.MenuUtil;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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

        return MenuUtil.getDailyMenuDTOList(responseObject);
    }


    // 일주일 일자별, 상세, 메뉴, 태그 정보 모두 저장
    @Override
    @Transactional
    public List<String> WeekMenuUpload(List<SubDailyMenuDTO> weekMenu){
        List<String> isSavedDateList = new ArrayList<>();
        //일일메뉴
        for(SubDailyMenuDTO dailyMenuDTO : weekMenu){

            SubDailyMenu savedDailyMenu = new SubDailyMenu();
            savedDailyMenu.setMenuDate(dailyMenuDTO.getMenuDate());
            savedDailyMenu.setHolidayFg(dailyMenuDTO.getHolidayFg());
            savedDailyMenu.setRestaurantOpenFg(dailyMenuDTO.getRestaurantOpenFg());

            //해당 날짜로 이미 존재하는 데이터 있다면 취소
            SubDailyMenu dm = subMenuDao.searchDailyMenu(dailyMenuDTO.getMenuDate());
            if(dm != null) continue;

            // 저장되면 저장날짜 반환하기
            isSavedDateList.add(dailyMenuDTO.getMenuDate().toString());

            // 하위 Details Entity들의 참조를 위해 일일메뉴 저장
            savedDailyMenu = subMenuDao.insertDailyMenu(savedDailyMenu);

            //Details(상세식단) 안에 있는 브릿지로 Details, menuItem 선 저장 후 bridge까지 한번에 저장하는 로직
            for(SubMenuDetailsDTO menuDetailsDTO : dailyMenuDTO.getMenuDetailsList()){
                SubMenuDetails savedMenuDetails = new SubMenuDetails();
                savedMenuDetails.setSubDailyMenu(savedDailyMenu);                               //일일메뉴데이터(Entity)
                savedMenuDetails.setDailyMenuDate(dailyMenuDTO.getMenuDate());                  //식단날짜
                savedMenuDetails.setDailyMenuCategory(menuDetailsDTO.getDailyMenuCategory());   //식단카테고리
                savedMenuDetails.setMainMealYn(menuDetailsDTO.getMainMealYn());                 //메인식단여부
                savedMenuDetails.setMenuDetailsName(menuDetailsDTO.getMenuDetailsName());       //식단이름

                //하위 Bridge Entity들의 참조를 위해 상세식단 저장
                savedMenuDetails = subMenuDao.insertMenuDetails(savedMenuDetails);

                // birdge 한번에 저장을 위해 List생성
                List<SubMenuDetailsItemBridge> savedBridgeList = new ArrayList<>();

                for(SubMenuDetailsItemBridgeDTO bridgeDTO : menuDetailsDTO.getSubBridgeList()){
                    SubMenuItem savedMenuItem = subMenuDao.menuItemCountUpAndSave(bridgeDTO.getMenuItemName());
                    // 만약 메뉴 이름이 ""라서 null이 반환됐다면 bridge를 포함항 menuItem데이터 저장하지 않아야함
                    if(savedMenuItem == null) continue;

                    //메인 메뉴에 속한 새로운 메뉴라면 New 태그 저장 (횟수가 1번일때가 처음 저장된 메뉴)
                    if(menuDetailsDTO.getMainMealYn().equals("Y") && savedMenuItem.getFrequencyCnt() == 1){
                        SubMenuTag newMenuTag = new SubMenuTag();
                        newMenuTag.setSubMenuItem(savedMenuItem);
                        newMenuTag.setMenuTagName("NEW");
                        // 15일뒤 날짜 가져오기
                        Timestamp tagEndDate = MenuUtil.getTimestampAfterNdays(LocalDate.now(), 15);
                        newMenuTag.setTagEndDate(tagEndDate);
                        subMenuDao.insertMenuTag(newMenuTag);
                    }

                    savedMenuItem.setMainMenuYn(bridgeDTO.getMainMenuYn()); //같은 영속성에 있기 떄문에 따로 추가 저장안해도 bridge 저장할때 같이 반영됨
                    savedMenuItem.setRecentMenuDetailsId(savedMenuDetails.getMenuDetailsId());


                    SubMenuDetailsItemBridge savedBridge = new SubMenuDetailsItemBridge();
                    savedBridge.setMainMenuYn(bridgeDTO.getMainMenuYn());
                    savedBridge.setMenuItemName(bridgeDTO.getMenuItemName());
                    savedBridge.setSubMenuDetails(savedMenuDetails);
                    savedBridge.setSubMenuItem(savedMenuItem);

                    savedBridgeList.add(savedBridge);
                }

                //bridge List 저장
                subMenuDao.insertBridgeList(savedBridgeList);
            }
        }

        return isSavedDateList;
    }
    //주간 식단메뉴 가져오기
    @Override
    public List<SubDailyMenuDTO> searchDailyMenuOfWeek(LocalDate date) {
        List<LocalDate> weekDays =  MeokuUtil.getWeekdaysInWeek(date);

        //해당주간의 시작, 끝 날짜 추출
        LocalDate startDate = weekDays.get(0);
        LocalDate endDate = weekDays.get(weekDays.size()-1);

        // 시작일과 종료일을 Timestamp로 변환
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        return subMenuDao.searchDailyMenuOfWeek(startTimestamp, endTimestamp);
    }

    // 원하는 날짜 식단 데이터 전부 삭제
    @Override
    public void deleteMenuData(LocalDate delDate) {
        Timestamp deleteDate = Timestamp.valueOf(delDate.atStartOfDay());
        subMenuDao.deleteMenuData(deleteDate);
    }

    // menuItem Id로 tag 가져오기
    @Override
    public List<SubMenuItemDTO> searchMenuTag(List<Integer> menuIdList) {
        return subMenuDao.searchMenuTag(menuIdList);
    }


}
