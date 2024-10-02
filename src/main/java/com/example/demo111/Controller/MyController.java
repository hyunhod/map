package com.example.demo111.Controller;


import com.example.demo111.Repository.RankingRepository;

import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.service.ApiService;
import com.example.demo111.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/import")
    public String aa(){
        apiService.fetchAndSaveApartmentData("C:/Users/black/OneDrive/바탕 화면/수도권코드.txt");
        return "search2";

    }



    @GetMapping("/")
    public String showMainPage(Model model) {

        List<TransactionRanking> ranking = rankingService.getTop10BySggCd("11");


        // 모델에 추천 아파트(Top 10) 추가
        model.addAttribute("recommendedApartments", ranking);
        return "main";
    }
    @GetMapping("/ranking")
    public String showRankingsPage() {

        return "search2";
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
                            @RequestParam(defaultValue = "0") int page,  // 페이지 번호
                            @RequestParam(defaultValue = "10") int size, // 페이지 크기
                            Model model) {


        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        // 페이지 값 검증
        if (page < 0) {
            model.addAttribute("errorMessage", "페이지 번호는 0 이상이어야 합니다.");
            return "search"; // 오류 시 다시 검색 페이지로
        }

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
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);


        // 정렬 기준에 따라 데이터베이스에서 결과 가져오기
        Page<TransactionRanking> sortedRankings;
        if ("price".equals(sortBy)) {
            sortedRankings = rankingService.getRankingsByDealAmount(pageable); // 건축일 기준으로 정렬된 랭킹

        } else {
            sortedRankings = rankingService.getRankingsByBuildYear(pageable);
        }

        model.addAttribute("rankings", sortedRankings);
        model.addAttribute("totalCount", rankings.size());
        model.addAttribute("totalPages", sortedRankings.getTotalPages());
        model.addAttribute("currentPage", sortedRankings.getNumber());
        model.addAttribute("locationName", locationName);
        model.addAttribute("dealYmd", dealYmd);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("buildYear", buildYear);
        model.addAttribute("minArea", minArea);
        model.addAttribute("maxArea", maxArea);
        model.addAttribute("sortBy", sortBy);


        // 페이지네이션 범위 계산
        int startPage = Math.max(0, sortedRankings.getNumber() - 5); // 최소 0 페이지
        int endPage = Math.min(sortedRankings.getTotalPages() - 1, sortedRankings.getNumber() + 4); // 최대 총 페이지 수

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        long duration = endTime - startTime; // 응답 시간 계산
        System.out.println("응답 시간: " + duration + "ms");

        return "result"; // 결과 페이지 이름
    }
    // 데이터 가져와서 저장하는 엔드포인트





}
