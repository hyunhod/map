package com.example.demo111.aprtDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    @JacksonXmlProperty(localName = "header")
    public HeaderDto header;
    @JacksonXmlProperty(localName = "body")
    public BodyDto body;


}
