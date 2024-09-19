package com.example.demo111.Controller;


import com.example.demo111.Dto.ResponseDto;
import com.example.demo111.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@Controller
public class MyController {
    private final ApiService apiService;

    // 생성자를 통해 ApiService를 주입받습니다.
    public MyController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetch-data")
    public ResponseDto fetchData(
            @RequestParam String lawdCd,
            @RequestParam String dealYmd,
            @RequestParam int pageNo,
            @RequestParam int numOfRows) {
        return apiService.fetchData(lawdCd, dealYmd, pageNo, numOfRows);
    }
}
