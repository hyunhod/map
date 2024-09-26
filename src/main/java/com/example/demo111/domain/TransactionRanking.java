package com.example.demo111.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class TransactionRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String AptNm;
    private String buildYear;
    private Integer dealAmount;
    private String dealYear;
    private Double excluUseAr;
    private String lawd_cd;


}
