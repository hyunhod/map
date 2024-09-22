package com.example.demo111.lawdCodDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 인식하지 못하는 필드를 무시
@AllArgsConstructor
@NoArgsConstructor
public class LawdCodeDto {



    @JacksonXmlProperty(localName = "region_cd")
    private String regionCode;

    @JacksonXmlProperty(localName = "sido_cd")
    private String sidoCode;

    @JacksonXmlProperty(localName = "sgg_cd")
    private String sggCode;

    @JacksonXmlProperty(localName = "umd_cd")
    private String umdCode;

    @JacksonXmlProperty(localName = "ri_cd")
    private String riCode;

    @JacksonXmlProperty(localName = "locatjumin_cd")
    private String locatJuminCode;

    @JacksonXmlProperty(localName = "locatjijuk_cd")
    private String locatJijukCode;

    @JacksonXmlProperty(localName = "locatadd_nm")
    private String locatAddName;

    @JacksonXmlProperty(localName = "locat_order")
    private String locatOrder;

    @JacksonXmlProperty(localName = "locat_rm")
    private String locatRm;

    @JacksonXmlProperty(localName = "locathigh_cd")
    private String locatHighCode;

    @JacksonXmlProperty(localName = "locallow_nm")
    private String locatLowName;

    @JacksonXmlProperty(localName = "adpt_de")
    private String adptDe;
}
