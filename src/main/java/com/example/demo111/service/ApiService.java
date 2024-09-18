package com.example.demo111.service;


import com.example.demo111.Dto.ResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    public ApiService() {
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public ResponseEntity<String> get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML); // XML 응답을 기대하므로 ContentType을 APPLICATION_XML로 설정

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

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

    public ResponseDto fetchData(String url) {
        // 1. XML 데이터를 받아오기
        ResponseEntity<String> responseEntity = get(url);
        String xmlData = responseEntity.getBody();

        // 2. XML 데이터를 파싱하기
        return parseXml(xmlData);
    }
}


