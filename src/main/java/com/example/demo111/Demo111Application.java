package com.example.demo111;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

        WebClient webClient = WebClient.create();
        webClient.get()
                .uri("https://apis.data.go.kr/1613000/RTMSDataSvcAptTrade/getRTMSDataSvcAptTrade?serviceKey=8xExb8lrSoH0CZUnRl63I5bo6DnnOfqaVNusp6RPXDmw1XyoV1pW%2BwjBbs84USEoZtAUg61ymXQh%2FsE5uIX3HQ%3D%3D&LAWD_CD=11110&DEAL_YMD=202407&pageNo=1&numOfRows=1")
                .retrieve()
                .bodyToMono(String.class)
                .map(xmlString -> {
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

                        // Document를 JSON으로 변환
                        String jsonString = convertXmlDocumentToJson(doc);
                        System.out.println(jsonString); // 결과 출력
                        return jsonString;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{}"; // 에러 발생 시 빈 JSON 반환
                    }
                })
                .subscribe();
    }

    // 정적 메소드로 변경
    private static String convertXmlDocumentToJson(Document doc) {
        try {
            // XML Document를 문자열로 변환
            String xmlString = convertDocumentToString(doc);
            System.out.println(xmlString);

            // XML 문자열을 JSON으로 변환
            XmlMapper xmlMapper = new XmlMapper();
            ObjectMapper jsonMapper = new ObjectMapper();

            // XML 문자열을 Java 객체로 변환
            Object object = xmlMapper.readValue(xmlString, Object.class);

            // Java 객체를 JSON 문자열로 변환
            return jsonMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // 에러 발생 시 빈 JSON 반환
        }
    }

    // Document를 문자열로 변환하는 메소드
    private static String convertDocumentToString(Document doc) throws Exception {
        javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
        javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(doc);
        StringWriter writer = new StringWriter();
        javax.xml.transform.stream.StreamResult streamResult = new javax.xml.transform.stream.StreamResult(writer);
        transformer.transform(domSource, streamResult);
        return writer.getBuffer().toString();
    }
}
