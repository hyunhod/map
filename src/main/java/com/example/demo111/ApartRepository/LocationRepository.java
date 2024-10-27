package com.example.demo111.ApartRepository;

import com.example.demo111.ApartDomain.Location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<Location> findByRegionCode(String regionCode); // 지역 코드로 검색

    // 아래 메서드를 수정하여 cityOrDistrict를 검색하도록 함
    List<Location> findByCityDistrict(String cityOrDistrict); // Optional로 반환하도록 수정



    List<Location> findAll();  // 상위 아파트 위치 목록 반환



    // 지역 코드로 페이징된 거래 정보 가져오기
    Page<Location> findByRegionCode(String regionCode, Pageable pageable);





}
