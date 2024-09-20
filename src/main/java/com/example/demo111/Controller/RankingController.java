package com.example.demo111.Controller;

import com.example.demo111.domain.TransactionRanking;
import com.example.demo111.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class RankingController {
    @Autowired
    private RankingService service;


}
