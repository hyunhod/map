package com.example.demo111.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApartmentDto {
    @JacksonXmlProperty(localName = "aptNm")// XML 태그와 DTO 필드를 명시적으로 매핑
    private String aptNm;
    @JacksonXmlProperty(localName = "buildYear")
    private String buildYear;
    @JacksonXmlProperty(localName = "dealAmount")
    private String dealAmount;
}
