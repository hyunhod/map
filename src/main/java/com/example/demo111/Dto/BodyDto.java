package com.example.demo111.Dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class BodyDto {
    @JacksonXmlProperty(localName = "items")
    public List<ItemsDto> items;
    public int numOfRows;
    public int pageNo;
    public int totalCount;
}
