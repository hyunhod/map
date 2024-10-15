package com.example.demo111.ApartService;


import com.example.demo111.ApartRepository.RankingRepository;
import com.example.demo111.aprtApiDto.BodyDto;
import com.example.demo111.aprtApiDto.ItemsDto;
import com.example.demo111.aprtApiDto.ResponseDto;
import com.example.demo111.ApartDomain.TransactionRanking;
import com.example.demo111.lawdCodApiDto.LawdCodeDto;
import com.example.demo111.lawdCodApiDto.LawdCodeResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.*;


@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;
    @org.springframework.beans.factory.annotation.Value("${api.base.url}")
    private String baseUrl;

    @Autowired
    RankingService rankingService;
    @Autowired
    RankingRepository rankingRepository;




    @Value("${apartment.data.file.path}")
    private String filePath;
    @Value("${api.service.key}")
    private String serviceKey;

    @Value("${api.base2.url}")
    private String base2Url;

    @Value("${api.service2.key}")
    private String service2Key;


    public ApiService() {
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public ResponseEntity<String> get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML); // XML 응답을 기대하므로 ContentType을 APPLICATION_XML로 설정

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, String.class);

        return response;
    }

    public ResponseDto parseXml(String xml) {
        ResponseDto response = null;
        try {
            response = xmlMapper.readValue(xml, ResponseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    // API 호출 (pageNo, numOfRows는 필수가 아니므로 기본값 설정 가능)

    public ResponseDto fetchData(List<String> lawdCodes, String dealYmd) {
        ResponseDto combinedResponse = new ResponseDto();
        BodyDto combinedBody = new BodyDto(); // BodyDto 객체를 생성
        List<ItemsDto> combinedItems = new ArrayList<>(); // 결합된 ItemsDto 리스트

        for (String lawdCd : lawdCodes) {
            int defaultNumOfRows = 10; // 기본값 설정

            ResponseDto initialResponse = fetchData(lawdCd, dealYmd, defaultNumOfRows);
            if (initialResponse == null || initialResponse.getBody() == null) {
                continue; // 응답이 없을 경우 다음 코드로 진행
            }

            int totalCount = initialResponse.getBody().getTotalCount(); // totalCount 값을 가져오기

            ResponseDto fetchedData = fetchData(lawdCd, dealYmd, totalCount);
            if (fetchedData != null && fetchedData.getBody() != null) {
                List<ItemsDto> items = fetchedData.getBody().getItems(); // fetchedData의 items를 가져옴
                if (items != null) {
                    combinedItems.addAll(items); // 리스트에 데이터 추가
                }
            }
        }

        combinedBody.setItems(combinedItems); // 결합된 items 리스트를 combinedBody에 설정
        combinedBody.setTotalCount(combinedItems.size()); // 총 개수를 설정
        combinedResponse.setBody(combinedBody); // combinedResponse에 combinedBody 설정

        return combinedResponse; // 결합된 응답 반환
    }


    public ResponseDto fetchData(String lawdCd, String dealYmd, int numOfRows) {
        // URL을 수동으로 생성
        String url = String.format("%s?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s&numOfRows=%d",
                baseUrl,
                serviceKey,
                lawdCd,
                dealYmd,
                numOfRows);



        ResponseEntity<String> responseEntity = get(url);
        if (responseEntity == null || responseEntity.getBody() == null) {
            System.out.println("No response body received.");
            return null;
        }
        String xmlData = responseEntity.getBody();


        // 2. XML 데이터를 파싱하기
        return parseXml(xmlData);
    }

    public ResponseDto fetchDataByLocationName(String locationName, String dealYmd) {
        try {
            // 지역 이름으로 지역 코드 가져오기
            LawdCodeResponseDto lawdCodeResponse = fetchLawdCodes(locationName);
            if (lawdCodeResponse == null || lawdCodeResponse.getLawdCodes().isEmpty()) {
                System.out.println("법정동 코드 정보를 찾을 수 없습니다.");
                return null;
            }


            // Set을 사용하여 중복을 제거
            Set<String> lawdCodesSet = new HashSet<>();
            for (LawdCodeDto lawdCodeDto : lawdCodeResponse.getLawdCodes()) {
                String lawdCd = lawdCodeDto.getRegionCode();
                if (lawdCd.length() >= 5) {
                    lawdCd = lawdCd.substring(0, 5); // 앞의 5자리만 사용
                    lawdCodesSet.add(lawdCd); // 유효한 코드만 리스트에 추가
                } else {
                    System.out.println("지역 코드가 5자리 미만입니다.");
                    return null; // 오류 처리
                }
            }
            // Set을 리스트로 변환
            List<String> lawdCodes = new ArrayList<>(lawdCodesSet);
            System.out.println("regionCode: " + lawdCodes);

            // 아파트 데이터 가져오기
            return fetchData(lawdCodes, dealYmd);
        } catch (Exception e) {
            System.out.println("API 호출 중 에러 발생: " + e.getMessage());
            return null;
        }
    }


    //api lawcod code
    public ResponseEntity<String> get2(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("Accept-Charset", "UTF-8");  // UTF-8 인코딩을 요청

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, String.class);
        return response;
    }

    public LawdCodeResponseDto fetchLawdCodes(String locationName) {
        int pageNo = 1; // 기본 페이지 번호
        int numOfRows = 3; // 기본 행 수
        String encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8); // 한글 인코딩

        String url = String.format("%s?ServiceKey=%s&type=xml&pageNo=%d&numOfRows=%d&flag=Y&locatadd_nm=%s",
                base2Url, service2Key, pageNo, numOfRows, encodedLocationName); // URL 생성


        ResponseEntity<String> responseEntity = get2(url);
        if (responseEntity == null || responseEntity.getBody() == null) {
            System.out.println("No response body received.");
            return null;
        }


        String xmlData = responseEntity.getBody();


        return parseLawdCodeResponse(xmlData); // XML 응답 파싱
    }

    public LawdCodeResponseDto parseLawdCodeResponse(String xmlData) {

        // XML 데이터를 LawdCodeDto로 변환
        try {
            LawdCodeResponseDto responseDto = xmlMapper.readValue(xmlData, LawdCodeResponseDto.class);


            return responseDto;
        } catch (Exception e) {
            System.err.println("Failed to parse XML: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }


    //.txt에서 행정코드 가져오기

    public List<String> readLawdCodesFromFile(String filePath) {
        Set<String> lawdCodesSet = new LinkedHashSet<>(); // 중복을 제거하기 위해 LinkedHashSet 사용
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // 첫 줄인지 확인하는 변수

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 첫 줄은 건너뜁니다.
                    continue;
                }

                String[] parts = line.split("\t"); // 탭으로 분리
                if (parts.length > 0) {
                    String lawdCode = parts[0].trim(); // 법정동코드
                    if (!lawdCode.isEmpty()) {
                        // 앞 5자리만 추가
                        String truncatedLawdCode = lawdCode.substring(0, 5);
                        lawdCodesSet.add(truncatedLawdCode); // Set에 추가하여 중복 제거
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(lawdCodesSet); // List로 변환하여 반환
    }


    //@Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void fetchAndSaveApartmentData(String filePath) {
        try {
            List<String> lawdCodes = readLawdCodesFromFile(filePath);
            System.out.println("추출된값 :"+lawdCodes);
            int currentYear = Year.now().getValue(); // 현재 연도를 가져옵니다.
            for (String lawdCode : lawdCodes) {
                for (int year = 2024; year <= currentYear; year++) {
                    for (int month = 5; month <= 6; month++) { // 1월부터 12월까지 반복
                        String dealYmd = String.format("%04d%02d", year, month); // 연도와 월을 사용
                        ResponseDto responseDto = fetchData(lawdCode, dealYmd, Integer.MAX_VALUE);
                        if (responseDto != null) {
                            List<TransactionRanking> rankings = rankingService.mapToTransactionRanking(responseDto);
                             saveTransactionRankings(rankings);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 에러 메시지를 출력합니다.
        }
    }
    public void saveTransactionRankings(List<TransactionRanking> rankings) {
        Set<TransactionRanking> uniqueRankings = new HashSet<>();  // 중복을 방지하기 위한 Set

        for (TransactionRanking ranking : rankings) {
            // 중복 체크
            if (!rankingRepository.existsBySggCdAndDealYearAndDealMonthAndDealDayAndAptNmAndDealAmountAndExcluUseArAndDealingGbnAndJibunAndFloorAndBuyerGbnAndSlerGbn(
                    ranking.getSggCd(), ranking.getDealYear(), ranking.getDealMonth(),ranking.getDealDay(), ranking.getAptNm(),ranking.getDealAmount(),ranking.getExcluUseAr(),ranking.getDealingGbn(),ranking.getJibun(),ranking.getFloor(),ranking.getBuyerGbn(),ranking.getSlerGbn())) {
                uniqueRankings.add(ranking);  // 중복이 아닌 경우 Set에 추가
            }
        }
        int batchSize = 10000;  // 배치 크기를 10,000으로 설정
        List<TransactionRanking> uniqueList = new ArrayList<>(uniqueRankings);

        // 중복이 제거된 데이터베이스에 저장
        for (int i = 0; i < uniqueList.size(); i += batchSize) {
            List<TransactionRanking> batchList = uniqueList.subList(i, Math.min(i + batchSize, uniqueList.size()));
            rankingRepository.saveAll(batchList);  // 배치로 데이터 저장
        }

    }

}







