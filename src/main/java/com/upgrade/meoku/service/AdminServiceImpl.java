package com.upgrade.meoku.service;

import com.upgrade.meoku.config.NaverCloudConfig;
import com.upgrade.meoku.data.dto.MeokuDailyMenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService{

    private final RestTemplate restTemplate; //NAVER 외부 API 호출을 위한 객체
    private final NaverCloudConfig naverCloudConfig; //NAVER 외부 API 호출을 위한 정보
    @Autowired
    public AdminServiceImpl(RestTemplate restTemplate,
                            NaverCloudConfig naverCloudConfig) {
        this.restTemplate = restTemplate;
        this.naverCloudConfig = naverCloudConfig;
    }

    @Override
    public List<MeokuDailyMenuDTO> MenuImageUploadAndReturnMenu(MultipartFile menuImage) throws Exception {

        if (menuImage.isEmpty()) { //이미지 없으면 에러
            //에러 발생
            throw new Exception();
        }

        // 이미지 파일을 바이트 배열로 변환하여 ByteArrayResource로 래핑
        ByteArrayResource imageResource = new ByteArrayResource(menuImage.getBytes()) {
            @Override
            public String getFilename() {
                return menuImage.getOriginalFilename();
            }
        };

        // 멀티파트 폼 데이터에 이미지 파일과 추가 정보 추가
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("files", new ByteArrayResource[]{imageResource});

        // 요청 JSON 데이터 구성
        Map<String, Object> imageInfo = new HashMap<>();
        imageInfo.put("format", "png");
        imageInfo.put("name", "demo");
        imageInfo.put("templateIds", new Integer[]{naverCloudConfig.getTEMPLATEIDS()});

        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("images", new Map[]{imageInfo});
        requestJson.put("requestId", UUID.randomUUID().toString());
        requestJson.put("version", "V1");
        requestJson.put("timestamp", System.currentTimeMillis());

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-OCR-SECRET", naverCloudConfig.getNaverApiScretKey());

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        // POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(naverCloudConfig.getNaverOcrUrl(), requestEntity, Map.class);
        Map<String, Object> responseObject = responseEntity.getBody();


        return null;
    }
    @Override
    public void WeekMenuUpload(List<MeokuDailyMenuDTO> weekMenu) {

    }
}
