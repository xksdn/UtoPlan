package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.CORS.JwtUtil;
import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import UtoPlan.UtoPlan.Model.PlanDTO;
import UtoPlan.UtoPlan.Model.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com", "https://utoplan.framer.website/"}) // CORS 설정
@RestController
@RequestMapping("/api")
public class MyPageController {

    @Autowired
    private JwtUtil jwtUtil;

    private final PlanService planService;

    //필드 주입은 테스트하기가 어려워지고, 의존성 설정이 완료되기 전까지 객체가 완전히 초기화되지 않기 때문에 권장되지 않다
    // 생성자 주입 방식
    public MyPageController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping("/my-page")
    public ResponseEntity<?> getPlansByUserId(HttpServletRequest request) {
        // Authorization 헤더에서 "Bearer "를 제거하고 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Missing or invalid Authorization header"));
        }

        // "Bearer " 이후의 실제 JWT 토큰 값 추출
        String token = authorizationHeader.substring(7).trim();
        System.out.println(token);

        Long userNum;
        try {
            // JWT 토큰에서 사용자 ID를 추출
            userNum = jwtUtil.extractNumFromToken(token);
        } catch (Exception e) {
            System.out.println("JWT 파싱 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid JWT Token", "error", e.getMessage()));
        }

        // 사용자의 플랜 데이터를 서비스에서 가져옴
        List<PlanDTO> plans = planService.getPlansByUserId(userNum);
        Map<String, Double> midpoint = planService.calculateMidpoint(plans); // 위도와 경도 계산

        // TripEntity 정보를 가져와서 필요한 값 추출
        TripEntity tripEntity = planService.getTripEntityByUserId(userNum); // 이 메서드는 사용자 ID로 TripEntity를 찾아오는 메서드여야 합니다.

        if (tripEntity == null) {
            // TripEntity가 null이면 로그로 남기고 응답 데이터에 포함하지 않음
            log.error("TripEntity not found for user ID: {}", userNum);
        }

        // URL 생성: 위도와 경도를 포함하여 날짜와 성인 수를 포함
        String hotelUrl = String.format(
                "https://kr.hotels.com/Hotel-Search?latLong=%f,%f&startDate=%s&endDate=%s&adults=%d&rooms=1&sort=RECOMMENDED",
                midpoint.get("latitude"),
                midpoint.get("longitude"),
                tripEntity != null ? tripEntity.getStartDate() : LocalDate.now(), // tripEntity가 null이면 기본값 사용
                tripEntity != null ? tripEntity.getEndDate() : LocalDate.now(),   // tripEntity가 null이면 기본값 사용
                tripEntity != null ? tripEntity.getAdult() : 1                    // tripEntity가 null이면 기본값 1 사용
        );

        // 응답 데이터 준비
        Map<String, Object> response = new HashMap<>();
        response.put("plans", plans);
        response.put("urls", Map.of(
                "flightUrl", "https://www.example.com/flight-booking", // 임시 URL
                "hotelUrl", hotelUrl // 동적으로 생성된 호텔 URL
        ));

        System.out.println("마이페이지 데이터 전송 성공");
        return ResponseEntity.ok(response);
    }


}