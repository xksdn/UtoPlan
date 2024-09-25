package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.DB.InquiryEntity;
import UtoPlan.UtoPlan.DB.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com"})
@RequestMapping("/api/inquiry")
public class InquiryApiController {

    private final InquiryRepository inquiryRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveInquiry(@RequestBody InquiryEntity inquiryEntity) {
        log.info("Received Inquiry: {}", inquiryEntity);  // 로그 출력
        inquiryRepository.save(inquiryEntity);  // DB에 저장
        return ResponseEntity.ok("Inquiry data saved successfully");
    }
}
