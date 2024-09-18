package com.example.demo111.service;

import com.example.demo111.Dto.ApartmentDto;
import com.example.demo111.Dto.ResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClient;
    private final String baseUrl = "https://apis.data.go.kr/1613000/RTMSDataSvcAptTrade/getRTMSDataSvcAptTrade";

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }
    // 동적으로 url 생성해서 데이터 받아오기
    public Mono<ResponseDto> fetchDataFromApi(String serviceKey,String lawdCd,String dealYmd,int pageNo,int numOfRows) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("LAWD_CD", lawdCd)
                        .queryParam("DEAL_YMD", dealYmd)
                        .queryParam("pageNo", pageNo)
                        .queryParam("numOfRows", numOfRows)
                        .build())
                .retrieve()
                .bodyToMono(String.class)  // XML 데이터를 문자열로 받아옴
                .flatMap(xmlData -> {
                    // XML 데이터를 DTO로 변환하는 과정
                    System.out.println("Received XML: " + xmlData);
                    try {
                        XmlMapper xmlMapper = new XmlMapper();
                        ResponseDto responseDto = xmlMapper.readValue(xmlData, ResponseDto.class);
                        return Mono.just(responseDto);  // DTO를 Mono로 감싸서 반환
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("XML error", e));  // 에러 처리
                    }
                });
    }

}
