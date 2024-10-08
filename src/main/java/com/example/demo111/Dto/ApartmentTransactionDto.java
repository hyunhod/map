package com.example.demo111.Dto;

import com.example.demo111.domain.TransactionRanking;
import lombok.Data;

import java.util.List;

@Data
public class ApartmentTransactionDto {
    private String aptNm;
    private List<TransactionRanking> transactions;
}
