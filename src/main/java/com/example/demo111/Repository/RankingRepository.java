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




    @Query("SELECT tr.aptNm, MAX(tr.dealAmount), tr.excluUseAr, tr.estateAgentSggNm " +
            "FROM TransactionRanking tr " +
            "WHERE tr.sggCd LIKE CONCAT(:sggCd, '%') " +
            "AND tr.dealYear = :year " +
            "AND tr.dealMonth = :month " +
            "GROUP BY tr.aptNm, tr.excluUseAr, tr.estateAgentSggNm " +
            "ORDER BY MAX(tr.dealAmount) DESC")
    List<Object[]> findTop10BySggCdAndDealAmount(
            @Param("sggCd") String sggCd,
            @Param("year") String year,
            @Param("month") String month,
            Pageable pageable);

    boolean existsBySggCdAndDealYearAndDealMonthAndDealDayAndAptNmAndDealAmountAndExcluUseArAndDealingGbnAndJibunAndFloorAndBuyerGbnAndSlerGbn(
            String sggCd, String dealYear, String dealMonth, String dealDay, String aptNm,
            Integer dealAmount, Double excluUseAr, String dealingGbn, String jibun, String floor,
            String buyerGbn, String slerGbn);



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


    @Query("SELECT tr.aptNm, COUNT(tr) AS transactionCount " +
            "FROM TransactionRanking tr " +
            "GROUP BY tr.aptNm " +
            "ORDER BY transactionCount DESC")
    List<Object[]> findAllTopAptsByTransactionCount();

}
