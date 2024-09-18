package com.example.demo111.Dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemsDto {
    @JacksonXmlProperty(localName = "item")
    private List<ApartmentDto> item;  // 여러 개의 아파트 정보를 받을 수 있으므로, 리스트로 만들 수도 있음
}
