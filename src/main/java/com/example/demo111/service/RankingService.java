package com.example.demo111.service;

import com.example.demo111.aprtDto.ItemsDto;
import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.Repository.RankingRepository;
import com.example.demo111.domain.TransactionRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;



    // ResponseDto를 TransactionRanking으로 매핑하는 메소드
    public List<TransactionRanking> mapToTransactionRanking(ResponseDto responseDto) {
        if (responseDto == null || responseDto.getBody().getItems() == null) {
            return null;
        }

        // TransactionRanking 리스트를 담을 객체 생성
        List<TransactionRanking> rankings = new ArrayList<>();

        // 각 ItemsDto를 TransactionRanking으로 변환
        for (ItemsDto item : responseDto.getBody().getItems()) {
            TransactionRanking ranking = new TransactionRanking();
            ranking.setAptNm(item.getAptNm());
            ranking.setBuildYear(item.getBuildYear());
            ranking.setDealAmount(item.getDealAmount());

            // 리스트에 추가
            rankings.add(ranking);
        }

        return rankings;
    }


    // 거래 금액 순으로 랭킹을 조회
    public List<TransactionRanking> getRankingsByDealAmount() {
        return rankingRepository.findAllByOrderByDealAmountDesc();
    }

    // 건설 연도 순으로 랭킹을 조회
    public List<TransactionRanking> getRankingsByBuildYear() {
        return rankingRepository.findAllByOrderByBuildYearDesc();
    }


}
