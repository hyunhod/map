package com.example.demo111.Controller;

import com.example.demo111.Dto.ApartmentDto;
import com.example.demo111.Dto.ItemsDto;
import com.example.demo111.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MyController {
    private final ApiService apiService;

    public MyController(ApiService apiService) {
        this.apiService = apiService;
    }

    // HTTP GET 요청 시 데이터 가져오기
    @GetMapping("/apartment-data")
    public Mono<String> getApartmentData(@RequestParam String serviceKey,
                                         @RequestParam String lawdCd,
                                         @RequestParam String dealYmd,
                                         @RequestParam int pageNo,
                                         @RequestParam int numOfRows) {
        return apiService.fetchDataFromApi(serviceKey, lawdCd, dealYmd, pageNo, numOfRows)
                .flatMap(responseDto -> {
                    // items에서 첫 번째 항목을 가져옴
                    if (responseDto != null && responseDto.getBody() != null &&
                            responseDto.getBody().getItems() != null &&
                            responseDto.getBody().getItems().getItem() != null &&
                            !responseDto.getBody().getItems().getItem().isEmpty()) {
                        ApartmentDto itemDto = responseDto.getBody().getItems().getItem().get(0);
                        return Mono.just("아파트 이름: " + itemDto.getAptNm() + ", 가격: " + itemDto.getDealAmount());
                    } else {
                        return Mono.just("아파트 정보가 없습니다.");
                    }
                });
    }
}
