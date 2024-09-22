package com.example.demo111.Controller;


import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.lawdCodDto.LawdCodeDto;
import com.example.demo111.lawdCodDto.LawdCodeResponseDto;
import com.example.demo111.service.ApiService;
import com.example.demo111.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class MyController {
    @Autowired
    private RankingService service;
    @Autowired
    private ApiService apiService;
    @Autowired
    private RankingService rankingService;


    // 생성자를 통해 ApiService를 주입받습니다.
    public MyController(ApiService apiService) {
        this.apiService = apiService;
    }


    @GetMapping("/fetch-data")
    public String fetchData(@RequestParam String locationName, @RequestParam String dealYmd, Model model) {
        ResponseDto responseDto = apiService.fetchDataByLocationName(locationName, dealYmd);

        if (responseDto == null) {
            model.addAttribute("errorMessage", "데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "search"; // 에러 발생 시 다시 검색 페이지로
        }
        List<TransactionRanking> rankings = rankingService.mapToTransactionRanking(responseDto);

        model.addAttribute("rankings", rankings);
        return "result"; // 결과 페이지 이름
    }


}
