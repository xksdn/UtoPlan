package UtoPlan.UtoPlan.DB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입을 처리하는 메서드
    public UserEntity registerUser(UserEntity user) {
        // 중복 사용자 검사 (ID를 기준으로)
        if (getUserById(user.getUser_id()).isPresent()) {
            throw new IllegalArgumentException("ID가 이미 존재하는 사용자입니다.");
        }

        // 사용자 저장
        return userRepository.save(user);
    }

    // 사용자 조회
    public Optional<UserEntity> getUserById(String id) {
        return userRepository.findById(id);
    }
}