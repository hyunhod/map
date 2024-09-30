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
    // 거래 금액으로 내림차순 정렬하여 조회

    Page<TransactionRanking> findAllByAptNmContaining(String aptNm, Pageable pageable);
    Page<TransactionRanking> findAllByOrderByDealAmountDesc(Pageable pageable);

    // 건설 연도 기준으로 내림차순 정렬하여 조회
    Page<TransactionRanking> findAllByOrderByBuildYearDesc(Pageable pageable);


    //222
    Page<TransactionRanking> findBySggCd(String sggCd,Pageable pageable); // 지역 코드로 거래 정보 찾기

    // 지역 코드와 거래 가격 범위를 기준으로 필터링
    Page<TransactionRanking> findBySggCdAndDealAmountBetween(String sggCd, Integer minPrice, Integer maxPrice, Pageable pageable);

    // 최소 가격 필터링
    Page<TransactionRanking> findBySggCdAndDealAmountGreaterThanEqual(String sggCd, Integer minPrice, Pageable pageable);

    // 최대 가격 필터링
    Page<TransactionRanking> findBySggCdAndDealAmountLessThanEqual(String sggCd, Integer maxPrice, Pageable pageable);

    // 추가된 메소드: 면적 필터링
    Page<TransactionRanking> findBySggCdAndExcluUseArBetween(String sggCd, Integer minArea, Integer maxArea, Pageable pageable);
    Page<TransactionRanking> findBySggCdAndExcluUseArGreaterThanEqual(String sggCd, Integer minArea, Pageable pageable);
    Page<TransactionRanking> findBySggCdAndExcluUseArLessThanEqual(String sggCd, Integer maxArea, Pageable pageable);


    // 추가된 메소드: 거래 날짜 필터링
    Page<TransactionRanking> findBySggCdAndDealYearAndDealMonthAndDealDay(String sggCd, Integer year, Integer month, Integer day, Pageable pageable);

    // 추가된 메소드: 정렬 기준에 따른 검색
    Page<TransactionRanking> findBySggCdOrderByDealAmountDesc(String sggCd, Pageable pageable);

    Page<TransactionRanking> findBySggCdOrderByBuildYearDesc(String sggCd, Pageable pageable);



}
