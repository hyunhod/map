package com.example.demo111.Controller;


import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.service.ApiService;
import com.example.demo111.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyController {
    @Autowired
    private RankingService service;
    @Autowired
    private  ApiService apiService;
    @Autowired
    private  RankingService rankingService;


    // 생성자를 통해 ApiService를 주입받습니다.
    public MyController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetch-data")
    public  String fetchData(
            @RequestParam String lawdCd,
            @RequestParam String dealYmd,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "1") int numOfRows, Model model) {
        ResponseDto rankings = apiService.fetchData(lawdCd, dealYmd, pageNo, numOfRows);
        List<TransactionRanking> rankings1 = rankingService.mapToTransactionRanking(rankings);

        model.addAttribute("rankings",rankings1);
        return "result";
    }





}
