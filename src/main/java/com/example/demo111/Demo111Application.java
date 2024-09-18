package com.example.demo111;

import com.example.demo111.Dto.ResponseDto;
import com.example.demo111.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.io.StringWriter;

@SpringBootApplication
public class Demo111Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo111Application.class, args);
    }

    @Bean
    public CommandLineRunner run(ApiService apiService) {
        return args -> {
            // API URL을 설정합니다.
            String url = "https://apis.data.go.kr/1613000/RTMSDataSvcAptTrade/getRTMSDataSvcAptTrade?serviceKey=8xExb8lrSoH0CZUnRl63I5bo6DnnOfqaVNusp6RPXDmw1XyoV1pW%2BwjBbs84USEoZtAUg61ymXQh%2FsE5uIX3HQ%3D%3D&LAWD_CD=11110&DEAL_YMD=202407&pageNo=1&numOfRows=1";

            // 데이터 가져오기
            ResponseDto response = apiService.fetchData(url);

            // 결과 출력
            System.out.println("Parsed Response: " + response);
        };
    }
}
