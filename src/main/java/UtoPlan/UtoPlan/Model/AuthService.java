package UtoPlan.UtoPlan.Model;

import UtoPlan.UtoPlan.Model.LoginRequest;
import UtoPlan.UtoPlan.Model.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public LoginResponse authenticate(LoginRequest loginRequest) {
        // 사용자 인증 로직 (예: 데이터베이스 조회)

        // 인증 성공 시 LoginResponse 객체 생성
        LoginResponse response = new LoginResponse();

        // 추가적인 필드 설정

        return response; // LoginResponse 객체 반환
    }
}