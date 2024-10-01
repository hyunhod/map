package com.example.demo111.controller;

import com.example.demo111.domain.Location;
import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.service.ApiService;
import com.example.demo111.service.LocationService;
import com.example.demo111.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class SearchController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RankingService rankingService;


    //db에 지역코드와 이름 저장후 불러오기
    @GetMapping("/search2")
    public String search(Model model) {
        //locationService.saveLocationsFromFile("C:/Users/black/OneDrive/바탕 화면/수도권코드.txt");
        Map<String, Set<String>> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);
        return "search2"; // search.html로 이동
    }

    @GetMapping("/sub-locations")
    @ResponseBody
    public Set<String> getSubLocations(@RequestParam String regionCode) {
        // 앞 2자리 (예: "11")을 사용하여 세부 지역 가져오기
        String regionPrefix = regionCode.substring(1, 3);
        System.out.println(regionPrefix);
        return locationService.getSubLocationsByRegion(regionPrefix);
    }

    // 아파트 거래 정보를 조회하는 메서드
    @GetMapping("/search1")
    public String searchTransactions(@RequestParam String locationName,
                                     @RequestParam(required = false) Integer minPrice,
                                     @RequestParam(required = false) Integer maxPrice,
                                     @RequestParam(required = false) Integer minArea,
                                     @RequestParam(required = false) Integer maxArea,
                                     @RequestParam(required = false) String dealDate,
                                     @RequestParam(required = false, defaultValue = "dealAmount") String sortBy,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {

        // 지역명으로 지역 정보를 조회
        List<Location> locations = locationService.findLocationByCityOrDistrict(locationName);
        System.out.println("locations :" + locations);
        List<TransactionRanking> paginatedTransactions = new ArrayList<>();
        int totalTransactions = 0;

        for (Location location : locations) {
            String regionCode = location.getRegionCode(); // 지역 코드 얻기
            System.out.println("Fetching transactions for region: " + regionCode);

            // 각 지역에 대해 페이지별로 거래 정보를 가져옴
            Page<TransactionRanking> transactionRankings = rankingService.getTransactionRankingsByRegion(regionCode, page, size, minPrice, maxPrice, minArea, maxArea, dealDate, sortBy);

            // 현재 페이지에 해당하는 데이터만 추가
            paginatedTransactions.addAll(transactionRankings.getContent());
            totalTransactions += transactionRankings.getTotalElements(); // 전체 거래 수 업데이트
        }

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalTransactions / size);
        System.out.println("Total Pages: " + totalPages);

        // 모델에 결과와 페이징 정보를 추가
        model.addAttribute("transactions", paginatedTransactions); // 거래 리스트
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수
        model.addAttribute("currentPage", page); // 현재 페이지
        model.addAttribute("locationName", locationName);
        model.addAttribute("minPrice", minPrice); // 최소 가격 필터 유지
        model.addAttribute("maxPrice", maxPrice); // 최대 가격 필터 유지
        model.addAttribute("minArea", minArea);
        model.addAttribute("maxArea", maxArea);
        model.addAttribute("dealDate", dealDate);
        model.addAttribute("sortBy", sortBy);

        return "transactionResults"; // 결과를 표시할 HTML 페이지로 이동
    }


}