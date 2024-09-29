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
    @JacksonXmlProperty(localName = "dealYear")
    private String dealYear;
    @JacksonXmlProperty(localName = "dealMonth")
    private String dealMonth;
    @JacksonXmlProperty(localName = "dealDay")
    private String dealDay;

    @JacksonXmlProperty(localName = "estateAgentSggNm")
    private String estateAgentSggNm;
    @JacksonXmlProperty(localName = "floor")
    private String floor;
    @JacksonXmlProperty(localName = "jibun")
    private String jibun;
    @JacksonXmlProperty(localName = "umdNm")
    private String umdNm;
    @JacksonXmlProperty(localName = "slerGbn")
    private String slerGbn;
    @JacksonXmlProperty(localName = "buyerGbn")
    private String buyerGbn;
    @JacksonXmlProperty(localName = "dealingGbn")
    private String dealingGbn;
    @JacksonXmlProperty(localName = "sggCd")
    private String sggCd;

}
