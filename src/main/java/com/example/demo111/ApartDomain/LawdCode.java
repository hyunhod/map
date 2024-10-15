package com.example.demo111.ApartDomain;

import com.example.demo111.lawdCodApiDto.LawdCodeResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class LawdCode {
    private List<LawdCodeResponseDto> lawdCodes;
    private String lawdCd;
    private String lawdNm;
}
