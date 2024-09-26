package com.example.demo111.service;


import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.lawdCodDto.LawdCodeDto;
import com.example.demo111.lawdCodDto.LawdCodeResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;
    @org.springframework.beans.factory.annotation.Value("${api.base.url}")
    private String baseUrl;

    @Value("${api.service.key}")
    private String serviceKey;

    @Value("${api.base2.url}")
    private String base2Url;

    @Value("${api.service2.key}")
    private String service2Key;


    public ApiService() {
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public ResponseEntity<String> get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML); // XML 응답을 기대하므로 ContentType을 APPLICATION_XML로 설정

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, String.class);

        return response;
    }

    public ResponseDto parseXml(String xml) {
        ResponseDto response = null;
        try {
            response = xmlMapper.readValue(xml, ResponseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("reponse: "+response);
        return response;
    }
    // API 호출 (pageNo, numOfRows는 필수가 아니므로 기본값 설정 가능)

    public ResponseDto fetchData(String lawdCd, String dealYmd) {
        int defaultNumOfRows = 10; // 기본값 설정

        ResponseDto initialResponse = fetchData(lawdCd, dealYmd, defaultNumOfRows);
        if (initialResponse == null || initialResponse.getBody() == null) {
            return null; // 응답이 없을 경우
        }
        // 2단계: totalCount를 가져와서 numOfRows에 설정
        int totalCount = initialResponse.getBody().getTotalCount(); // totalCount 값을 가져오기

        System.out.println("total count: "+totalCount);
        // 3단계: totalCount를 numOfRows로 설정하여 다시 API 호출
        return fetchData(lawdCd, dealYmd, totalCount);

    }


    public ResponseDto fetchData(String lawdCd, String dealYmd, int numOfRows) {
        // URL을 수동으로 생성
        String url = String.format("%s?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s&numOfRows=%d",
                baseUrl,
                serviceKey,
                lawdCd,
                dealYmd,
                numOfRows);

        System.out.println("url : "+url);

        ResponseEntity<String> responseEntity = get(url);
        if (responseEntity == null || responseEntity.getBody() == null) {
            System.out.println("No response body received.");
            return null;
        }
        String xmlData = responseEntity.getBody();
        System.out.println("xmlData: " + xmlData);

        System.out.println("xmlData: "+xmlData);

        // 2. XML 데이터를 파싱하기
        return parseXml(xmlData);
    }

    public ResponseDto fetchDataByLocationName(String locationName, String dealYmd) {
       try {
           // 지역 이름으로 지역 코드 가져오기
           LawdCodeResponseDto lawdCodeResponse = fetchLawdCodes(locationName);
           if (lawdCodeResponse == null || lawdCodeResponse.getLawdCodes().isEmpty()) {
               System.out.println("법정동 코드 정보를 찾을 수 없습니다.");
               return null;
           }

           // 첫 번째 지역 코드 사용, 앞의 5자리만 추출
           String lawdCd = lawdCodeResponse.getLawdCodes().get(0).getRegionCode();
           if (lawdCd.length() >= 5) {
               lawdCd = lawdCd.substring(0, 5); // 앞의 5자리만 사용
           } else {
               System.out.println("지역 코드가 5자리 미만입니다.");
               return null; // 오류 처리
           }

           // 아파트 데이터 가져오기
           return fetchData(lawdCd, dealYmd);
       }catch (Exception e){
           System.out.println("API 호출 중 에러 발생: "+e.getMessage());
           return null;
       }
    }



    //api lawcod code
    public ResponseEntity<String> get2(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("Accept-Charset", "UTF-8");  // UTF-8 인코딩을 요청

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, String.class);
        return response;
    }

    public LawdCodeResponseDto fetchLawdCodes(String locationName) {
        int pageNo = 1; // 기본 페이지 번호
        int numOfRows = 3; // 기본 행 수
        String encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8); // 한글 인코딩

        String url = String.format("%s?ServiceKey=%s&type=xml&pageNo=%d&numOfRows=%d&flag=Y&locatadd_nm=%s",
                base2Url, service2Key, pageNo, numOfRows, encodedLocationName); // URL 생성
        System.out.println("받은 location name: "+encodedLocationName);
        System.out.println("url: "+url);

        ResponseEntity<String> responseEntity = get2(url);
        if (responseEntity == null || responseEntity.getBody() == null) {
            System.out.println("No response body received.");
            return null;
        }
        System.out.println("responseEntity: "+responseEntity);

        String xmlData = responseEntity.getBody();

        System.out.println("xmlData : "+xmlData);
        return parseLawdCodeResponse(xmlData); // XML 응답 파싱
    }

    public LawdCodeResponseDto parseLawdCodeResponse(String xmlData) {

        // XML 데이터를 LawdCodeDto로 변환
        try {
            LawdCodeResponseDto responseDto = xmlMapper.readValue(xmlData, LawdCodeResponseDto.class);
            System.out.println("value: "+responseDto);

            return responseDto;
        } catch (Exception e) {
            System.err.println("Failed to parse XML: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}


