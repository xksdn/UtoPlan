package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.DB.AuthService;
import UtoPlan.UtoPlan.DB.UserEntity;
import UtoPlan.UtoPlan.DB.UserService;
import UtoPlan.UtoPlan.Model.LoginRequest;
import UtoPlan.UtoPlan.Model.LoginResponse;
import UtoPlan.UtoPlan.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com", "https://utoplan.framer.website/"}) // CORS 설정
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService; // AuthService는 로그인 로직을 처리하는 서비스

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, @RequestHeader(value = "Authorization", required = false) String authorization) {
        LoginResponse response = new LoginResponse();

        // Authorization 체크 (Basic 인증 방식 사용)
        if (authorization == null || !authorization.equals("Basic " + Base64.getEncoder().encodeToString("user:987456321".getBytes()))) {
            response.setSuccess(false);
            response.setMessage("Unauthorized");
            return response;
        }

        // 로그인 로직 처리
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        boolean isAuthenticated = userService.login(email, password);

        if (isAuthenticated) {
            // 로그인 성공 시 JWT 토큰 생성
            Long num = userService.getUserNumByEmail(email);
            String token = jwtUtil.generateToken(String.valueOf(num)); // num을 String으로 변환

            response.setSuccess(true);
            response.setMessage("로그인 성공");
            response.setToken(token);  // JWT 토큰 반환
        } else {
            response.setSuccess(false);
            response.setMessage("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return response; // LoginResponse 객체 반환
    }
}