package com.example.demo111.Repository;

import com.example.demo111.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,String> {
    Optional<Location> findByRegionCode(String regionCode); // 지역 코드로 검색

    // 특정 앞자리로 시작하는 지역 코드 검색
    List<Location> findByRegionCodeStartingWith(String prefix);
}
