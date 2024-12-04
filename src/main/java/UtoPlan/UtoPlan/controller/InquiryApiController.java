package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.CORS.JwtUtil;
import UtoPlan.UtoPlan.DB.Entity.InquiryEntity;
import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.DB.Repository.InquiryRepository;
import UtoPlan.UtoPlan.DB.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com", "https://utoplan.framer.website/"})
@RequestMapping("/api/inquiry")
public class InquiryApiController {

    private final InquiryRepository inquiryRepository;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveInquiry(@RequestBody InquiryEntity inquiryEntity, HttpServletRequest request) {
        try {
            // Authorization 헤더에서 JWT 토큰 추출
            String token = request.getHeader("Authorization").substring("Basic ".length()).trim();
            Long userId = jwtUtil.extractUserId(token);

            // user_id를 UserEntity에서 찾기
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            inquiryEntity.setUser(user);
            // DB에 저장
            inquiryRepository.save(inquiryEntity);
            return ResponseEntity.ok("Inquiry data saved successfully");
        } catch (Exception e) {
            log.error("Error processing the inquiry: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error processing the inquiry");
        }
    }

    // GET 요청: 모든 문의 데이터를 반환
    @GetMapping("/all")
    public ResponseEntity<List<InquiryEntity>> getAllInquiries() {
        log.info("Fetching all inquiries..."); // 요청이 들어왔을 때 로그 확인
        try {
            List<InquiryEntity> inquiries = inquiryRepository.findAll();
            log.info("Fetched inquiries: {}", inquiries); // 서버에서 가져온 데이터 확인
            return ResponseEntity.ok(inquiries);
        } catch (Exception e) {
            log.error("Error fetching inquiries: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

//    // GET 요청: 특정 사용자의 문의 데이터를 반환
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<InquiryEntity>> getInquiriesByUserId(@PathVariable Long userId) {
//        try {
//            List<InquiryEntity> inquiries = inquiryRepository.findByUserId(userId);
//            return ResponseEntity.ok(inquiries);
//        } catch (Exception e) {
//            log.error("Error fetching inquiries for user {}: {}", userId, e.getMessage());
//            return ResponseEntity.status(500).body(null);
//        }
//    }

}
