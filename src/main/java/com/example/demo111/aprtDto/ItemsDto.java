package com.example.demo111.aprtDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsDto {
    @JacksonXmlProperty(localName = "aptNm")// XML 태그와 DTO 필드를 명시적으로 매핑
    public String aptNm;
    @JacksonXmlProperty(localName = "buildYear")
    public String buildYear;
    @JacksonXmlProperty(localName = "dealAmount")
    public String dealAmount;
    @JacksonXmlProperty(localName = "excluUseAr")
    private String excluUseAr;
}
