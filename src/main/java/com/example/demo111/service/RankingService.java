package com.example.demo111.service;

import com.example.demo111.aprtDto.ItemsDto;
import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.Repository.RankingRepository;
import com.example.demo111.domain.TransactionRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;





    // ResponseDto를 TransactionRanking으로 매핑하는 메소드
    public List<TransactionRanking> mapToTransactionRanking(ResponseDto responseDto) {
        //rankingRepository.deleteAll();
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
            ranking.setDealYear(item.getDealYear());
            ranking.setFloor(item.getFloor());
            ranking.setUmdNm(item.getUmdNm());
            ranking.setJibun(item.getJibun());
            ranking.setDealDay(item.getDealDay());
            ranking.setBuyerGbn(item.getBuyerGbn());
            ranking.setDealMonth(item.getDealMonth());
            ranking.setEstateAgentSggNm(item.getEstateAgentSggNm());
            ranking.setSggCd(item.getSggCd());
            ranking.setSlerGbn(item.getSlerGbn());
            ranking.setDealingGbn(item.getDealingGbn());



            if (item.getDealAmount() != null && !item.getDealAmount().isEmpty()) {
               try{
                   String sanitizedAmount = item.getDealAmount().replace(",", ""); // 쉼표 제거
                   ranking.setDealAmount(Integer.parseInt(sanitizedAmount));
               }catch (NumberFormatException e){
                   ranking.setDealAmount(0);
               }
            }

            if (item.getExcluUseAr() != null && !item.getExcluUseAr().isEmpty()) {
                ranking.setExcluUseAr(Double.parseDouble(item.getExcluUseAr()));
            }
            //리스트에 추가
            rankings.add(ranking);
            System.out.println("rankings :: "+rankings);

        }


        return rankings;
    }
    // 매핑된 TransactionRanking 리스트를 데이터베이스에 저장하는 메소드
    public void saveTransactionRankings(List<TransactionRanking> rankings) {
        if (rankings != null && !rankings.isEmpty()) {
            rankingRepository.saveAll(rankings); // JPA의 saveAll 메소드 사용
        }
    }


    // 거래 금액 순으로 랭킹을 조회
    public Page<TransactionRanking> getRankingsByDealAmount(Pageable pageable) {

        return rankingRepository.findAllByOrderByDealAmountDesc(pageable);
    }

    // 건설 연도 순으로 랭킹을 조회
    public Page<TransactionRanking> getRankingsByBuildYear(Pageable pageable) {
        return rankingRepository.findAllByOrderByBuildYearDesc(pageable);
    }
    // 특정 아파트 이름으로 검색 및 페이징 처리
//    public Page<TransactionRanking> searchByAptNm(String aptNm, Pageable pageable) {
//        return rankingRepository.findByAptNmContaining(aptNm, pageable);
//    }

    // 거래 금액 순으로 상위 10개의 아파트만 조회하는 메서드
    public List<TransactionRanking> getTop10ByDealAmount() {
        // 페이지 크기 10으로 설정하고 첫 번째 페이지만 가져옴
        Pageable topTen = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dealAmount"));
        Page<TransactionRanking> rankingPage = rankingRepository.findAll(topTen);

        // 상위 10개의 데이터를 반환
        return rankingPage.getContent();
    }







}
