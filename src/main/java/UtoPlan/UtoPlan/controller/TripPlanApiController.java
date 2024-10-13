package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.CORS.JwtUtil;
import UtoPlan.UtoPlan.DB.Entity.PlanEntity;
import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.DB.Repository.TripRepository;
import UtoPlan.UtoPlan.Model.PlanService;
import UtoPlan.UtoPlan.openAPI.GoogleAPI.DayPlan;
import UtoPlan.UtoPlan.openAPI.GoogleAPI.GooglePlacesAPI;
import UtoPlan.UtoPlan.openAPI.GoogleAPI.TripPlanLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com", "https://utoplan.framer.website/"})
@RequestMapping("/api/trip")
public class TripPlanApiController {
    private final TripRepository tripRepository;
    private final TripPlanLogic tripPlanLogic;
    private final GooglePlacesAPI googlePlacesAPI;
    private final PlanService planService;
    private final JwtUtil jwtUtil;

    @PostMapping("/save")
    public ResponseEntity<String> saveTrip(
            @RequestBody TripEntity tripEntity
            ) {
        log.info("Received Inquiry: {}", tripEntity);
        tripRepository.save(tripEntity);
        return ResponseEntity.ok("Trip data saved successfully");
    }


    @PostMapping("/response")
    public ResponseEntity<String> getPlaces (
            @RequestBody TripRepository tripRepository
    ) {

        return ResponseEntity.ok("");
    }

    @PostMapping("/plan")
    public ResponseEntity<List<DayPlan>> sendPlan(
            @RequestHeader("Authorization") String token
    ) throws JsonProcessingException {
        // JWT 토큰에서 사용자 num 값을 추출
        UserEntity user;
        try {
            user = jwtUtil.getUserFromToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }

        // 사용자와 연관된 여행 정보 조회
        List<TripEntity> tripEntities = tripRepository.findByUser(user);
        if (tripEntities.isEmpty()) {
            throw new RuntimeException("No trip found for the user");
        }

        // 여러 여행 계획이 있을 경우 가장 최근의 계획을 가져오는 방법 (예시: 0번째 계획 가져오기)
        TripEntity tripEntity = tripEntities.get(0);

        log.info("Retrieved tripEntity for user {}: {}", user.getNum(), tripEntity);
        // 여행 계획을 기반으로 로직 처리
        List<DayPlan> dayPlans = tripPlanLogic.tripLogic(tripEntity);
        return ResponseEntity.ok(dayPlans);
    }


//    @PostMapping("/plan")
//    public ResponseEntity<List<DayPlan>> sendPlan () throws JsonProcessingException {
//        Long testUserId = 2001L;
//        TripEntity tripEntity = tripRepository.findById(testUserId)
//                        .orElseThrow(() -> new RuntimeException("Test user not found"));
//
//        log.info(" ", tripEntity);
//        List<DayPlan> dayPlans = tripPlanLogic.tripLogic(tripEntity);
//        return ResponseEntity.ok(dayPlans);
//    }

    @PostMapping("/plan-save")
    public ResponseEntity<String> savePlan(
            @RequestHeader("Authorization") String token,
            @RequestBody List<PlanEntity> plans
    ) {
        // JWT 토큰에서 사용자 num 값을 추출
        Long userNum;
        try {
            userNum = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }

        // 추출된 userNum을 PlanService에 전달하여 계획과 장소를 저장
        planService.savePlanWithPlaces(plans, userNum);
        return ResponseEntity.ok("Plans saved successfully");
    }
}
