package com.example.demo111.aprtDto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.persistence.Entity;
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
