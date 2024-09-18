//package com.example.demo111.Controller;
//
//
//import com.example.demo111.Dto.ResponseDto;
//import com.example.demo111.service.ApiService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.util.UriComponentsBuilder;
//import reactor.core.publisher.Mono;
//
//@RestController
//public class MyController {
//    private final ApiService apiService;
//
//    // 생성자를 통해 ApiService를 주입받습니다.
//    public MyController(ApiService apiService) {
//        this.apiService = apiService;
//    }
//
//    @GetMapping("/apartment-data")
//    public ResponseEntity<?> getApartmentData(
//            @RequestParam String serviceKey,
//            @RequestParam String lawdCd,
//            @RequestParam String dealYmd,
//            @RequestParam int pageNo,
//            @RequestParam int numOfRows) {
//
//        // URL을 동적으로 생성합니다.
//        String url = UriComponentsBuilder.fromHttpUrl("https://apis.data.go.kr/1613000/RTMSDataSvcAptTrade/getRTMSDataSvcAptTrade")
//                .queryParam("serviceKey", serviceKey)
//                .queryParam("LAWD_CD", lawdCd)
//                .queryParam("DEAL_YMD", dealYmd)
//                .queryParam("pageNo", pageNo)
//                .queryParam("numOfRows", numOfRows)
//                .encode()  // 인코딩 적용
//                .toUriString();
//
//        // ApiService를 통해 데이터를 가져옵니다.
//        ResponseDto response = apiService.fetchData(url);
//
//        // 응답을 클라이언트에게 반환합니다.
//        return ResponseEntity.ok(response);
//    }
//}
