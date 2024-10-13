package UtoPlan.UtoPlan.Model;

import UtoPlan.UtoPlan.DB.Entity.PlaceEntity;
import UtoPlan.UtoPlan.DB.Entity.PlanEntity;
import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.DB.Repository.PlaceRepository;
import UtoPlan.UtoPlan.DB.Repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlaceRepository placeRepository;

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

                    // 계획과 장소 연결
                    place.setPlan(plan);
                }
            } else {
                log.warn("No places found for this plan.");
            }

            // 계획 저장 (장소와 연관된 상태로)
            PlanEntity savedPlan = planRepository.save(plan);

            // 장소 저장
            if (plan.getPlaces() != null && !plan.getPlaces().isEmpty()) {
                for (PlaceEntity place : plan.getPlaces()) {
                    place.setPlan(savedPlan);  // 저장된 plan을 다시 설정하여 외래 키 연결 보장
                    placeRepository.save(place);
                }
            }
        }
    }
}