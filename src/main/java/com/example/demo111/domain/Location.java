package com.example.demo111.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "지역코드")
    private String regionCode;

    @Column(name = "지역명")
    private String name;
    @Column(name = "시/군")
    private String cityOrDistrict;

}
