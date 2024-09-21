package com.example.demo111.domain;

import com.example.demo111.lawdCodDto.LawdCodeResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class LawdCode {
    private List<LawdCodeResponseDto> lawdCodes;
    private String lawdCd;
    private String lawdNm;
}
