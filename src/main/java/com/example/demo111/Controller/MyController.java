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

//    @GetMapping("/fetch-data")
//    public String fetchData(
//            @RequestParam String lawdCd,
//            @RequestParam String dealYmd,
//            @RequestParam(defaultValue = "1") int pageNo,
//            @RequestParam(defaultValue = "1") int numOfRows, Model model) {
//        ResponseDto rankings = apiService.fetchData(lawdCd, dealYmd, pageNo, numOfRows);
//        List<TransactionRanking> rankings1 = rankingService.mapToTransactionRanking(rankings);
//
//        model.addAttribute("rankings", rankings1);
//        return "result";
//    }

    @GetMapping("/lawdcodes")
    public String getLawdCodes(@RequestParam("locationName") String locationName, Model model) {

        LawdCodeResponseDto response = apiService.fetchLawdCodes(locationName);

        System.out.println(response);
        System.out.println("location : "+locationName);
        if (response != null) {
            List<LawdCodeDto> lawdCodes = response.getLawdCodes();
            System.out.println("list값 : "+lawdCodes+" ,"+locationName);
            model.addAttribute("lawdCodes", lawdCodes);
            model.addAttribute("totalCount", response.getHead().getTotalCount());
        } else {
            model.addAttribute("error", "No data found.");
        }
        return "lawdCodeResult"; // Thymeleaf 템플릿 이름
    }



    @GetMapping("/fetch-data")
    public String fetchData(@RequestParam String locationName, @RequestParam String dealYmd, Model model) {
        ResponseDto responseDto = apiService.fetchDataByLocationName(locationName, dealYmd);
        List<TransactionRanking> rankings = rankingService.mapToTransactionRanking(responseDto);
        model.addAttribute("rankings", rankings);
        return "result"; // 결과 페이지 이름
    }


}
