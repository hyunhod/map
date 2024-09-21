package com.example.demo111.lawdCodDto;

import com.example.demo111.domain.LawdCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;


@Data
@JacksonXmlRootElement(localName = "StanReginCd")
@JsonIgnoreProperties(ignoreUnknown = true) // 인식하지 못하는 필드를 무시
public class LawdCodeResponseDto {
    @JacksonXmlProperty(localName = "head")
    private Head head;

    @JacksonXmlProperty(localName = "row")
    private List<LawdCodeDto> lawdCodes;
    // Getters and Setters

}

