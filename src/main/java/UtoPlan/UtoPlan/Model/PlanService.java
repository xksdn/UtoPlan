package UtoPlan.UtoPlan.Model;

import UtoPlan.UtoPlan.DB.Entity.PlaceEntity;
import UtoPlan.UtoPlan.DB.Entity.PlanEntity;
import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.DB.Repository.PlaceRepository;
import UtoPlan.UtoPlan.DB.Repository.PlanRepository;
import UtoPlan.UtoPlan.DB.Repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    private TripRepository tripRepository; // TripEntity를 찾기 위한 Repository

    // 사용자 ID로 TripEntity 찾기
    public TripEntity getTripEntityByUserId(Long userNum) {
        return tripRepository.findByUserNum(userNum)
                .orElse(null);  // 결과가 없으면 null 반환
    }




    public void savePlanWithPlaces(List<PlanEntity> plans, Long userId) {
        for (PlanEntity plan : plans) {
            log.info("Saving Plan: {}", plan);

            // 사용자 설정
            UserEntity user = UserEntity.builder().num(userId).build();
            plan.setUser(user);

            // 날짜 설정 (사용자가 제공한 날짜 사용)
            if (plan.getDate() == null) {
                throw new IllegalArgumentException("PlanEntity에 날짜가 설정되지 않았습니다. 날짜 값은 필수입니다.");
            }

            // 장소 리스트와 PlanEntity 연관 설정
            if (plan.getPlaces() != null && !plan.getPlaces().isEmpty()) {
                for (PlaceEntity place : plan.getPlaces()) {
                    // 저장 전 필드 검증 (필수 필드 검증)
                    if (place.getName() == null || place.getLatitude() == 0 || place.getLongitude() == 0) {
                        throw new IllegalArgumentException("PlaceEntity 필수 필드 누락");
                    }

                    // imageUrl 필드가 있는지 확인
                    if (place.getImageUrl() == null || place.getImageUrl().isEmpty()) {
                        throw new IllegalArgumentException("PlaceEntity의 imageUrl이 누락되었습니다.");
                    }

                    log.info("Saving Place: {}", place);

                    // 계획과 장소 연결
                    place.setPlan(plan);
                    log.info("Setting PlaceEntity with imageUrl: {}", place.getImageUrl());
                }
            } else {
                log.warn("No places found for this plan.");
            }

            // 계획 저장 (장소와 연관된 상태로)
            PlanEntity savedPlan = planRepository.save(plan);

            // 장소가 이미 연관된 상태에서 저장하므로 별도로 저장하지 않아도 됨
            log.info("Plan and associated places saved successfully.");
        }
    }


    // 사용자 ID에 따른 계획 리스트 가져오기
    //트랜잭션 확장
    @Transactional
    public List<PlanDTO> getPlansByUserId(Long userId) {
        List<PlanEntity> plans = planRepository.findByUser_Num(userId);

        // Null 체크
        if (plans == null || plans.isEmpty()) {
            log.warn("No plans found for userId: {}", userId);
            return Collections.emptyList();
        }

        return plans.stream().map(plan -> {
            List<PlaceDTO> places = plan.getPlaces().stream()
                    .map(place -> new PlaceDTO(place.getId(), place.getName(), place.getLatitude(), place.getLongitude(), place.getImageUrl()))
                    .collect(Collectors.toList());
            return new PlanDTO(plan.getId(), plan.getDay(), plan.getDate(), places);
        }).collect(Collectors.toList());
    }

    //최적의 위치 호텔을 검색하기 위한 위도, 경도의 중간값을 추출
    public Map<String, Double> calculateMidpoint(List<PlanDTO> plans) {
        // 위도와 경도를 저장할 리스트 초기화
        List<Double> latitudes = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();

        // 모든 PlaceDTO에서 위도와 경도 값을 추출
        for (PlanDTO plan : plans) {
            for (PlaceDTO place : plan.getPlaces()) {
                latitudes.add(place.getLatitude());
                longitudes.add(place.getLongitude());
            }
        }

        // 위도와 경도가 비어 있는지 확인
        if (latitudes.isEmpty() || longitudes.isEmpty()) {
            throw new IllegalArgumentException("No latitude or longitude values found");
        }

        // 위도와 경도의 중간값 계산
        double avgLatitude = latitudes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgLongitude = longitudes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        log.info("avgLatitude: {}", avgLatitude);
        log.info("avgLongitude: {}", avgLongitude);

        // 결과 반환
        return Map.of("latitude", avgLatitude, "longitude", avgLongitude);
    }

    //플랜 날짜의 최소 값과 최대 값을 구함
    public Map<String, LocalDate> findEarliestAndLatestDates(List<PlanDTO> plans) {
        // 날짜 리스트 추출
        List<LocalDate> dates = plans.stream()
                .map(PlanDTO::getDate) // PlanDTO에서 date 추출
                .collect(Collectors.toList());

        // Null 또는 빈 리스트 체크
        if (dates.isEmpty()) {
            throw new IllegalArgumentException("No dates found in plans");
        }

        // 가장 빠른 날짜와 가장 느린 날짜 계산
        LocalDate earliestDate = dates.stream().min(LocalDate::compareTo).orElse(null);
        LocalDate latestDate = dates.stream().max(LocalDate::compareTo).orElse(null);
        log.info("Earliest date: {}", earliestDate);
        log.info("Latest date: {}", latestDate);


        // 결과 반환
        return Map.of(
                "earliestDate", earliestDate,
                "latestDate", latestDate
        );
    }





}