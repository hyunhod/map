package com.example.demo111.lawdCodDto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
    @JacksonXmlProperty(localName = "resultCode")
    private String resultCode;

    @JacksonXmlProperty(localName = "resultMsg")
    private String resultMsg;
}
