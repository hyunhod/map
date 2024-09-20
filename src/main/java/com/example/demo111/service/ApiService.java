package com.example.demo111.service;


import com.example.demo111.Dto.ItemsDto;
import com.example.demo111.Dto.ResponseDto;
import com.example.demo111.domain.TransactionRanking;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;
    @org.springframework.beans.factory.annotation.Value("${api.base.url}")
    private String baseUrl;

    @Value("${api.service.key}")
    private String serviceKey;

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

}


