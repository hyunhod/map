package com.example.demo111.lawdCodDto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Head {
    @JacksonXmlProperty(localName = "totalCount")
    private int totalCount;

    @JacksonXmlProperty(localName = "numOfRows")
    private int numOfRows;

    @JacksonXmlProperty(localName = "pageNo")
    private int pageNo;

    @JacksonXmlProperty(localName = "type")
    private String type;
}

