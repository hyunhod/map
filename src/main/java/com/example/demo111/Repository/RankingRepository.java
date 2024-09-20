package com.example.demo111.Repository;

import com.example.demo111.domain.TransactionRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<TransactionRanking,Long> {
    // 거래 금액으로 내림차순 정렬하여 조회
    List<TransactionRanking> findAllByOrderByDealAmountDesc();

    // 건설 연도 기준으로 내림차순 정렬하여 조회
    List<TransactionRanking> findAllByOrderByBuildYearDesc();


}
