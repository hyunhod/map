package com.example.demo111.Repository;

import com.example.demo111.domain.TransactionRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<TransactionRanking,Long>, JpaSpecificationExecutor<TransactionRanking> {


    Page<TransactionRanking> findAllByOrderByDealAmountDesc(Pageable pageable);

    // 건설 연도 기준으로 내림차순 정렬하여 조회
    Page<TransactionRanking> findAllByOrderByBuildYearDesc(Pageable pageable);


    //222
    Page<TransactionRanking> findBySggCd(String sggCd,Pageable pageable); // 지역 코드로 거래 정보 찾기




}
