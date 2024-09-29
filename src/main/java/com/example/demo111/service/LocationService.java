package com.example.demo111.service;

import com.example.demo111.Repository.LocationRepository;
import com.example.demo111.domain.Location;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void saveLocationsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "EUC-KR"))) {
            String line;
            boolean isFirstLine = true; // 첫 줄인지 확인하는 변수

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 첫 줄은 건너뜁니다.
                    continue;
                }
                String[] fields = line.split("\t"); // 탭(\t) 구분자로 필드 나누기
                String regionCode = fields[0].substring(0, 5); // 지역 코드 앞 5자리
                String fullRegionName = fields[1]; // 전체 지역 이름

                // 지역명과 시/동 분리
                String[] regionParts = fullRegionName.split(" "); // 공백으로 분리
                String regionName = regionParts[0]; // 지역명 (예: 강원특별자치도)
                String cityOrDistrict = regionParts.length > 1 ? regionParts[1] : ""; // 시/군/구 (예: 춘천시)

                // 중복된 지역코드가 존재하는지 확인
                Optional<Location> existingLocation = locationRepository.findByRegionCode(regionCode);
                if (existingLocation.isPresent()) {
                    continue; // 이미 해당 지역코드가 존재하면 저장하지 않고 건너뜁니다.
                }


                // DB에 저장
                Location location = new Location();
                location.setRegionCode(regionCode);
                location.setName(regionName);
                location.setCityOrDistrict(cityOrDistrict);

                locationRepository.save(location); // JPA를 통해 데이터베이스에 저장
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    public Set<String> getSubLocationsByRegion(String regionPrefix) {
        List<Location> allLocations = locationRepository.findAll();
        return allLocations.stream()
                .filter(location -> location.getRegionCode().startsWith(regionPrefix))
                .map(Location::getCityOrDistrict)
                .collect(Collectors.toSet());
    }



    public Map<String, Set<String>> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .collect(Collectors.groupingBy(Location::getName,
                        Collectors.mapping(Location::getRegionCode, Collectors.toSet())));
    }
}
