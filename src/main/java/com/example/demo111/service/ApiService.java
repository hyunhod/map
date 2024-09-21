package com.example.demo111.service;


import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.lawdCodDto.LawdCodeDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


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
        return response;
    }
    // API 호출 (pageNo, numOfRows는 필수가 아니므로 기본값 설정 가능)
    public ResponseDto fetchData(String lawdCd, String dealYmd) {
        int defaultPageNo = 1; // 기본값 설정
        int defaultNumOfRows = 10; // 기본값 설정
        return fetchData(lawdCd, dealYmd, defaultPageNo, defaultNumOfRows);
    }

    public ResponseDto fetchData(String lawdCd, String dealYmd, int pageNo, int numOfRows) {
        // URL을 수동으로 생성
        String url = String.format("%s?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s&pageNo=%d&numOfRows=%d",
                baseUrl,
                serviceKey,
                lawdCd,
                dealYmd,
                pageNo,
                numOfRows);

        System.out.println("url : "+url);

        ResponseEntity<String> responseEntity = get(url);
        if (responseEntity == null || responseEntity.getBody() == null) {
            System.out.println("No response body received.");
            return null;
        }
        String xmlData = responseEntity.getBody();
        System.out.println("success");

        // 2. XML 데이터를 파싱하기
        return parseXml(xmlData);
    }


    //api lawcod code
    public ResponseEntity<String> get2(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, String.class);
    }

    public LawdCodeDto fetchLawdCodes(String locationName) {
        int pageNo = 1; // 기본 페이지 번호
        int numOfRows = 3; // 기본 행 수
        String url = String.format("%s/getStanReginCdList?ServiceKey=%s&type=xml&pageNo=%d&numOfRows=%d&flag=Y&locatadd_nm=%s",
                base2Url, service2Key, pageNo, numOfRows, locationName); // URL 생성

        ResponseEntity<String> responseEntity = get(url);
        if (responseEntity == null || responseEntity.getBody() == null) {
            System.out.println("No response body received.");
            return null;
        }

        String xmlData = responseEntity.getBody();
        return parseLawdCodeResponse(xmlData); // XML 응답 파싱
    }

    public LawdCodeDto parseLawdCodeResponse(String xml) {
        // XML 데이터를 LawdCodeDto로 변환
        try {
            return xmlMapper.readValue(xml, LawdCodeDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}


