package com.example.demo111.Controller;


import com.example.demo111.Repository.RankingRepository;
import com.example.demo111.aprtDto.BodyDto;
import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.lawdCodDto.LawdCodeDto;
import com.example.demo111.lawdCodDto.LawdCodeResponseDto;
import com.example.demo111.service.ApiService;
import com.example.demo111.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class MyController {
    @Autowired
    private RankingRepository rankingRepository;
    @Autowired
    private RankingService service;
    @Autowired
    private ApiService apiService;
    @Autowired
    private RankingService rankingService;


    @GetMapping("/")
    public String showRankingsPage() {

        return "search";
    }
    @GetMapping("map")
    public String map(){
        return "map";
    }

    // 생성자를 통해 ApiService를 주입받습니다.
    public MyController(ApiService apiService) {
        this.apiService = apiService;
    }


    @GetMapping("/fetch-data")
    public String fetchData(@RequestParam String locationName, @RequestParam String dealYmd,
                            @RequestParam(required = false) String buildYear,
                            @RequestParam(required = false) Integer minArea,
                            @RequestParam(required = false) Integer maxArea,
                            @RequestParam(required = false) Integer minPrice,
                            @RequestParam(required = false) Integer maxPrice,
                            @RequestParam(required = false, defaultValue = "price") String sortBy, // 기본값은 가격순
                            Model model) {
        ResponseDto responseDto = apiService.fetchDataByLocationName(locationName, dealYmd);

        if (responseDto == null) {
            model.addAttribute("errorMessage", "데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "search"; // 에러 발생 시 다시 검색 페이지로
        }
        List<TransactionRanking> rankings = rankingService.mapToTransactionRanking(responseDto);


        //건축년도 필터링 추가
        if (buildYear != null && !buildYear.isEmpty()) {
            rankings = rankings.stream()
                    .filter(r -> r.getBuildYear().equals(buildYear))
                    .collect(Collectors.toList());
        }

        // 전용면적 필터링 추가
        if (minArea != null && maxArea != null) {
            rankings = rankings.stream()
                    .filter(r -> r.getExcluUseAr() >= minArea && r.getExcluUseAr() <= maxArea)
                    .collect(Collectors.toList());
        }

        //거래 금액 필터링
        if (minPrice != null && maxPrice != null) {
            rankings = rankings.stream()
                    .filter(r -> r.getDealAmount() >= minPrice && r.getDealAmount() <= maxPrice)
                    .collect(Collectors.toList());
        }
        rankingService.saveTransactionRankings(rankings);


        // 정렬 기준에 따라 데이터베이스에서 결과 가져오기
        List<TransactionRanking> sortedRankings;
        if ("price".equals(sortBy)) {
            sortedRankings = rankingService.getRankingsByDealAmount(); // 건축일 기준으로 정렬된 랭킹
        } else {
            sortedRankings = rankingService.getRankingsByBuildYear();
        }

        model.addAttribute("rankings", sortedRankings);
        model.addAttribute("totalCount",rankings.size());
        return "result"; // 결과 페이지 이름
    }
    // 데이터 가져와서 저장하는 엔드포인트


}
