package com.example.demo111.lawdCodApiDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@JacksonXmlRootElement(localName = "StanReginCd")
@JsonIgnoreProperties(ignoreUnknown = true) // 인식하지 못하는 필드를 무시
@AllArgsConstructor
@NoArgsConstructor
public class LawdCodeResponseDto {
    @JacksonXmlProperty(localName = "head")
    private HeadDto head;

    @JacksonXmlProperty(localName = "row")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<LawdCodeDto> lawdCodes;

}

