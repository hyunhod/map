package com.example.demo111.Dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class BodyDto {
    @JacksonXmlProperty(localName = "items")
    private ItemsDto items;
}
