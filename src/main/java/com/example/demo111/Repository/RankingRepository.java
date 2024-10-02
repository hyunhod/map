package com.example.demo111.Repository;

import com.example.demo111.domain.TransactionRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<TransactionRanking,Long>, JpaSpecificationExecutor<TransactionRanking> {


    Page<TransactionRanking> findAllByOrderByDealAmountDesc(Pageable pageable);

    // 건설 연도 기준으로 내림차순 정렬하여 조회
    Page<TransactionRanking> findAllByOrderByBuildYearDesc(Pageable pageable);


    // sggCd로 가격이 가장 높은 상위 10개 거래 정보를 가져오는 메서드 추가
    @Query("SELECT distinct tr FROM TransactionRanking tr WHERE tr.sggCd LIKE CONCAT(:sggCd, '%') AND (tr.dealYear = :year1 AND tr.dealMonth >= :month1 OR tr.dealYear = :year2 AND tr.dealMonth <= :month2 OR tr.dealYear = :year3) ORDER BY tr.dealAmount DESC")
    Page<TransactionRanking> findTop10BySggCd(
            @Param("sggCd") String sggCd,
            @Param("year1") String year1,
            @Param("month1") String month1,
            @Param("year2") String year2,
            @Param("month2") String month2,
            @Param("year3") String year3,
            Pageable pageable
    );

    boolean existsBySggCdAndDealYearAndDealMonthAndAptNm(String sggCd, String dealYear, String dealMonth, String aptNm);




    //222
    Page<TransactionRanking> findBySggCd(String sggCd,Pageable pageable); // 지역 코드로 거래 정보 찾기

    // 아파트 이름으로 거래 내역을 조회하는 메소드
    List<TransactionRanking> findByAptNm(String AptName);




    @Query("SELECT tr.aptNm, COUNT(tr) AS transactionCount " +
            "FROM TransactionRanking tr " +
            "WHERE tr.sggCd = :regionCode " + // 지역 코드로 필터링
            "GROUP BY tr.aptNm " +
            "ORDER BY transactionCount DESC")
    List<Object[]> findTopAptsByTransactionCount(@Param("regionCode") String regionCode); // 메서드 이름 수정



}
