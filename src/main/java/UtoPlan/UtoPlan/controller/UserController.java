package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.CORS.JwtUtil;
import UtoPlan.UtoPlan.Model.UserProfileDto;
import UtoPlan.UtoPlan.Model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserProfileDto userProfileDto,
                                                @RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.extractNumFromToken(token);  // 토큰에서 사용자 ID 추출
            userService.updateUserProfile(userId, userProfileDto);
            return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로필 수정 중 오류가 발생했습니다.");
        }
    }
}

