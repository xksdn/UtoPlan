package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.Model.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com"}) // CORS 설정
@RestController
@RequestMapping("/api")
public class SignUpController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    private Map<String, String> verificationCodes = new HashMap<>();

    @PostMapping("/send-code")
    public Map<String, Object> sendVerificationCode(@RequestBody Map<String, String> emailRequest, @RequestHeader("Authorization") String authorization) {

        Map<String, Object> response = new HashMap<>();

        // Authorization 체크 (Basic 인증 방식 사용)
        if (!authorization.equals("Basic " + Base64.getEncoder().encodeToString("user:987456321".getBytes()))) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return response;
        }

        String email = emailRequest.get("email");
        log.info("Email: {}", email);

        if (email != null && !email.isEmpty()) {
            String code = generateSixDigitCode();
            verificationCodes.put(email, code);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("인증코드 " + code);
            message.setText("인증코드: " + code);
            mailSender.send(message);

            response.put("success", true);
            response.put("message", "Code sent successfully");
            response.put("code", code);
        } else {
            response.put("success", false);
            response.put("message", "Invalid email address");
        }

        return response;
    }


    @PostMapping("/sign-up")
    public Map<String, Object> signUp(@RequestBody Map<String, String> request,@RequestHeader("Authorization") String authorization) {

        Map<String, Object> response = new HashMap<>();


        // Authorization 체크 (Basic 인증 방식 사용)
        if (!authorization.equals("Basic " + Base64.getEncoder().encodeToString("user:987456321".getBytes()))) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return response;
        }

        String id = request.get("id");
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");


        if (id == null ||name == null || email == null || password == null) {
            response.put("success", false);
            response.put("message", "All fields are required");
            return response;
        }


        UserEntity user = UserEntity.builder()
                .user_id(id)
                .user_name(name)
                .user_password(password)
                .email(email)// 암호화 추가 가능
                .build();

        try {
            // 사용자 등록
            userService.registerUser(user);
            response.put("success", true);
            response.put("message", "User registered successfully");
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }


        return response;
    }

    private static final SecureRandom random = new SecureRandom();

    public static String generateSixDigitCode() {
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }
}
