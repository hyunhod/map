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
    private String dealAmount;
    private int transactionCount; //거래량
    private int transactionPrice; //거래가격
    private LocalDate transactionDate;

}
