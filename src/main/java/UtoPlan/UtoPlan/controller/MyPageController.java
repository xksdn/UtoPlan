package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.CORS.JwtUtil;
import UtoPlan.UtoPlan.Model.PlanDTO;
import UtoPlan.UtoPlan.Model.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        System.out.println("마이페이지 데이터 전송 성공");
        return ResponseEntity.ok(plans);
    }

}