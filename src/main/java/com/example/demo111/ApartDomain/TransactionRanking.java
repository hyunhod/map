package com.example.demo111.ApartDomain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TransactionRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="이름")
    private String aptNm;
    @Column(name="건축년도")
    private String buildYear;
    @Column(name="가격")
    private Integer dealAmount;
    @Column(name="년")
    private String dealYear;
    @Column(name="월")
    private String dealMonth;
    @Column(name="일")
    private String dealDay;
    @Column(name="전용면적")
    private Double excluUseAr;
    @Column(name="거래유형")
    private String dealingGbn;
    @Column(name="층")
    private String floor;
    @Column(name="중개사소재지")
    private String estateAgentSggNm;
    @Column(name="법정동")
    private String umdNm;
    @Column(name="지번")
    private String jibun;
    @Column(name="지역코드")
    private String sggCd;
    @Column(name="매수자")
    private String buyerGbn;
    @Column(name="매도자")
    private String slerGbn;



}
