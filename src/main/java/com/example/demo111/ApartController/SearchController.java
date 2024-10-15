package com.example.demo111.controller;

import com.example.demo111.ApartDomain.Location;
import com.example.demo111.ApartDomain.TransactionRanking;
import com.example.demo111.ApartService.ApiService;
import com.example.demo111.ApartService.LocationService;
import com.example.demo111.ApartService.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SearchController {


    @Autowired
    private ApiService apiService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RankingService rankingService;


    //db에 지역코드와 이름 저장후 불러오기
    @GetMapping("/aptRanking")
    public String search(@RequestParam(required = false) String locationName, Model model) {
        // locationService.saveLocationsFromFile("C:/Users/black/OneDrive/바탕 화면/수도권코드.txt");
        Map<String, Set<String>> locationsMap = locationService.getAllLocations();
        model.addAttribute("locations", locationsMap);

        List<Location> locations;
        if (locationName == null || locationName.isEmpty()) {
            // 기본 상위 아파트 데이터를 조회
            locations = locationService.findDefaultTopLocations(); // 기본 데이터 조회하는 메서드 추가

            List<String> finalTopApartments = new ArrayList<>();
            List<Long> transactionCounts = new ArrayList<>(); // 거래 건수를 저장할 리스트

            for (Location location : locations) {
                String regionCode = location.getRegionCode(); // 지역 코드 얻기

                // 상위 아파트 이름과 거래 수량 가져오기
                List<Object[]> topApartmentsWithCount = rankingService.getTopAptsByTransactionCount();

                for (Object[] result : topApartmentsWithCount) {
                    String apartmentName = (String) result[0];
                    Long transactionCount = (Long) result[1];

                    if (transactionCount > 0) { // 거래 건수가 0 이상인 아파트만 추가
                        finalTopApartments.add(apartmentName);
                        transactionCounts.add(transactionCount);
                    }
                }

            }

            // 필요한 경우 상위 20개 아파트만 선택
            finalTopApartments = finalTopApartments.stream()
                    .limit(20)
                    .collect(Collectors.toList());

            // 거래 건수의 최대값 계산( 막대도표 상대비교하기위해)
            Long maxTransactionCount = transactionCounts.stream()
                    .max(Long::compareTo) // Long 타입의 최대값 찾기
                    .orElse(1L); // 기본값 1L로 설정

            // 모델에 상위 아파트 목록과 거래 수 추가
            model.addAttribute("topApartments", finalTopApartments);
            model.addAttribute("transactionCounts", transactionCounts);
            model.addAttribute("maxTransactionCount", maxTransactionCount); // 최대값 모델에 추가


            return "/Apt/aptRankingResults"; // search.html로 이동
        } else {
            locations = locationService.findLocationByCityOrDistrict(locationName);


            List<String> finalTopApartments = new ArrayList<>();
            List<Long> transactionCounts = new ArrayList<>(); // 거래 건수를 저장할 리스트

            for (Location location : locations) {
                String regionCode = location.getRegionCode(); // 지역 코드 얻기

                // 상위 아파트 이름과 거래 수량 가져오기
                List<Object[]> topApartmentsWithCount = rankingService.getTopAptsByTransactionCount(regionCode);

                for (Object[] result : topApartmentsWithCount) {
                    String apartmentName = (String) result[0];
                    Long transactionCount = (Long) result[1];


                    if (transactionCount > 0) { // 거래 건수가 0 이상인 아파트만 추가
                        finalTopApartments.add(apartmentName);
                        transactionCounts.add(transactionCount);
                    }
                }
            }

            // 중복 제거를 위해 Set 사용
            Set<String> uniqueTopApartmentsSet = new HashSet<>(finalTopApartments);
            finalTopApartments = new ArrayList<>(uniqueTopApartmentsSet);

            // 필요한 경우 상위 20개 아파트만 선택
            finalTopApartments = finalTopApartments.stream()
                    .limit(20)
                    .collect(Collectors.toList());

            // 거래 건수의 최대값 계산( 막대도표 상대비교하기위해)
            Long maxTransactionCount = transactionCounts.stream()
                    .max(Long::compareTo) // Long 타입의 최대값 찾기
                    .orElse(1L); // 기본값 1L로 설정

            // 모델에 상위 아파트 목록과 거래 수 추가
            model.addAttribute("topApartments", finalTopApartments);
            model.addAttribute("transactionCounts", transactionCounts);
            model.addAttribute("maxTransactionCount", maxTransactionCount); // 최대값 모델에 추가

            return "/Apt/aptRankingResults"; // search.html로 이동
        }
    }


    @GetMapping("/sub-locations")
    @ResponseBody
    public Set<String> getSubLocations(@RequestParam String regionCode) {
        // 앞 2자리 (예: "11")을 사용하여 세부 지역 가져오기
        String regionPrefix = regionCode.substring(1, 3);

        return locationService.getSubLocationsByRegion(regionPrefix);
    }


    // 아파트 거래 정보를 조회하는 메서드
    @GetMapping("/aptSearch")
    public String searchTransactions(@RequestParam(required = false) String locationName,
                                     @PageableDefault(page = 0, size = 5) Pageable pageable,
                                     Model model) {
        Map<String, Set<String>> mapLocation = locationService.getAllLocations();
        model.addAttribute("locations", mapLocation);
        Map<String, List<TransactionRanking>> apartmentDetails = new HashMap<>();
        String searchedApartmentName = null;
            // 지역명으로 지역 정보를 조회
            List<Location> locations = locationService.findLocationByCityOrDistrict(locationName);
            Set<String> apartmentNames = new HashSet<>();

            // 모든 거래를 대상으로 아파트 이름 수집
            for (Location location : locations) {
                String regionCode = location.getRegionCode(); // 지역 코드 얻기
                List<TransactionRanking> allTransactions = rankingService.getAllTransactionsByRegion(regionCode);
                allTransactions.forEach(tr -> apartmentNames.add(tr.getAptNm()));
            }

            List<String> apartmentNamesList = new ArrayList<>(apartmentNames);
            int totalApartments = apartmentNamesList.size();

            // 페이지네이션된 아파트 이름 리스트
            List<String> pagedApartmentNames = apartmentNamesList.stream()
                    .skip(pageable.getPageNumber() * pageable.getPageSize())
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList());

            // 아파트 이름에 대한 거래 내역을 가져옴
            for (String aptNm : pagedApartmentNames) {
                List<TransactionRanking> transactions = rankingService.getTransactionsByAptNm(aptNm);
                apartmentDetails.put(aptNm, transactions);
            }

            // 전체 페이지 수와 현재 페이지 설정
            int totalPages = (int) Math.ceil((double) totalApartments / pageable.getPageSize());
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", pageable.getPageNumber());
            model.addAttribute("apartmentNames", pagedApartmentNames);
            model.addAttribute("locationName", locationName);

        model.addAttribute("apartmentDetails", apartmentDetails);
        return "/Apt/aptSearchResults"; // 결과를 표시할 HTML 페이지로 이동
    }

    @GetMapping("/price-history")
    @ResponseBody
    public List<TransactionRanking> getPriceHistoryByAptNm(@RequestParam String aptNm) {
        // 아파트 이름을 기준으로 거래 기록 조회
        List<TransactionRanking> transactions = rankingService.getTransactionsByAptNm(aptNm);

        // 거래 날짜 기준으로 정렬 (연도, 월, 일 기준)
        transactions.sort(Comparator.comparing(TransactionRanking::getDealYear)
                .thenComparing(TransactionRanking::getDealMonth)
                .thenComparing(TransactionRanking::getDealDay));


        // 정렬된 거래 기록 반환
        return transactions;
    }


    @GetMapping("/getApartmentDetails")
    @ResponseBody
    public Map<String, Object> getApartmentDetails(@RequestParam String name
            , @PageableDefault(page = 0, size = 5) Pageable pageable) {
        // 해당 아파트 이름에 따른 거래 정보를 조회
        Page<TransactionRanking> transactionsPage = rankingService.getTransactionsByAptNm(name,pageable);


        // 아파트 정보 반환
        Map<String, Object> response = new HashMap<>();
        List<TransactionRanking> transactions = transactionsPage.getContent();
        if (!transactions.isEmpty()) {
            // 첫 번째 거래 정보를 기준으로 법정동, 지번 등 기본 정보 제공
            TransactionRanking firstTransaction = transactions.get(0);
            response.put("umdNm", firstTransaction.getUmdNm());
            response.put("jibun", firstTransaction.getJibun());
            response.put("buildYear", firstTransaction.getBuildYear()); // 건축년도

        }

        // 거래 정보 리스트 추가
        response.put("transactions", transactions);
        response.put("currentPage", transactionsPage.getNumber());
        response.put("totalPages", transactionsPage.getTotalPages());
        return response;
    }



}