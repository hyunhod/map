package com.example.demo111.service;

import com.example.demo111.aprtDto.ItemsDto;
import com.example.demo111.aprtDto.ResponseDto;
import com.example.demo111.Repository.RankingRepository;
import com.example.demo111.domain.TransactionRanking;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
                try {
                    String sanitizedAmount = item.getDealAmount().replace(",", ""); // 쉼표 제거
                    ranking.setDealAmount(Integer.parseInt(sanitizedAmount));
                } catch (NumberFormatException e) {
                    ranking.setDealAmount(0);
                }
            }

            if (item.getExcluUseAr() != null && !item.getExcluUseAr().isEmpty()) {
                ranking.setExcluUseAr(Double.parseDouble(item.getExcluUseAr()));
            }
            //리스트에 추가
            rankings.add(ranking);
            System.out.println("rankings :: " + rankings);

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


    // 거래 금액 순으로 상위 10개의 아파트만 조회하는 메서드
    public List<TransactionRanking> getTop10ByDealAmount() {
        // 페이지 크기 10으로 설정하고 첫 번째 페이지만 가져옴
        Pageable topTen = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dealAmount"));
        Page<TransactionRanking> rankingPage = rankingRepository.findAll(topTen);

        // 상위 10개의 데이터를 반환
        return rankingPage.getContent();
    }

    // 지역 코드로 거래 정보 가져오기
    public Page<TransactionRanking> getTransactionRankingsByRegion(String regionCode, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size); // 페이지 번호와 페이지 크기를 설정
        return rankingRepository.findBySggCd(regionCode, pageRequest); // 지역 코드로 거래 정보 검색
    }

    public Page<TransactionRanking> getTransactionRankingsByRegion(String sggCd, int page, int size, Integer minPrice, Integer maxPrice, Integer minArea, Integer maxArea, String dealDate, String sortBy) {
        Pageable pageable = PageRequest.of(page, size);


        // 동적 쿼리 작성
        Specification<TransactionRanking> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // 모든 조건을 결합하는 Predicate 생성


            // 지역 코드 필터링 조건 추가
            if (sggCd != null && !sggCd.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("sggCd"), sggCd));
            }
            // 가격 필터링 조건을 추가
            if (minPrice != null && maxPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("dealAmount"), minPrice, maxPrice));
            } else if (minPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("dealAmount"), minPrice));
            } else if (maxPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("dealAmount"), maxPrice));
            }

            if (minArea != null && maxArea != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("excluUseAr"), minArea, maxArea));
            } else if (minArea != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("excluUseAr"), minArea));
            } else if (maxArea != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("excluUseAr"), maxArea));
            }
            // 거래 날짜 필터링 조건 적용
            if (dealDate != null && !dealDate.isEmpty()) {
                String[] dateParts = dealDate.split("-");
                Integer year = Integer.parseInt(dateParts[0]);
                Integer month = Integer.parseInt(dateParts[1]);
                Integer day = Integer.parseInt(dateParts[2]);

                // 거래 날짜 필터링 조건 추가 (예: "dealYear", "dealMonth", "dealDay" 필드 사용)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("dealYear"), year));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("dealMonth"), month));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("dealDay"), day));
            }
            return predicate;
        };
        // 정렬 기준을 포함한 Pageable 생성
        Sort sort;
        if ("buildYear".equals(sortBy)) {
            sort = Sort.by("buildYear").descending(); // buildYear로 정렬
        } else {
            sort = Sort.by("dealAmount").descending(); // 기본 정렬 기준
        }

        pageable = PageRequest.of(page, size, sort); // 정렬 기준을 포함한 Pageable 생성

        // 검색 결과 반환
        Page<TransactionRanking> result = rankingRepository.findAll(spec, pageable);

        System.out.println("Result: " + result);
        return result; // 검색 결과 반환
    }
}










