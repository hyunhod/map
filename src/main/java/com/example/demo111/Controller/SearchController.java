package com.example.demo111.controller;

import com.example.demo111.domain.Location;
import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.service.LocationService;
import com.example.demo111.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class SearchController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private RankingService rankingService;



    //db에 지역코드와 이름 저장후 불러오기
    @GetMapping("/search2")
    public String search(Model model) {
        //locationRepository.deleteAll();
        //locationService.saveLocationsFromFile("C:/Users/black/Downloads/법정동코드 전체자료 (1)/법정동코드 전체자료.txt");
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
    public String searchTransactions( @RequestParam String locationName,
                                     @RequestParam(required = false) Integer minPrice,
                                     @RequestParam(required = false) Integer maxPrice,
                                     Model model) {

        Location location = locationService.findLocationByCityOrDistrict(locationName);


        String regionCode = location.getRegionCode(); // 지역 코드 얻기
        System.out.println("regionCode: "+regionCode);

        // regionCode에 해당하는 거래 정보를 가져옵니다.
        List<TransactionRanking> transactionRankings = rankingService.getTransactionRankingsByRegion(regionCode);

        // 가격 필터링
        if (minPrice != null) {
            transactionRankings.removeIf(ranking -> ranking.getDealAmount() < minPrice);
        }
        if (maxPrice != null) {
            transactionRankings.removeIf(ranking -> ranking.getDealAmount() > maxPrice);
        }

        // 모델에 결과 추가
        model.addAttribute("transactions", transactionRankings);
        return "transactionResults"; // 결과를 표시할 HTML 페이지로 이동
    }

}
