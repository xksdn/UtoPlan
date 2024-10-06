package UtoPlan.UtoPlan.Model;

import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.DB.Repository.UserRepository;
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
        //if (getUserByUserId(user.getUser_id()).isPresent()) {
        //    throw new IllegalArgumentException("ID가 이미 존재하는 사용자입니다.");
        //}

        // 사용자 저장
        return userRepository.save(user);
    }
    // user_id로 조회
    //public Optional<UserEntity> getUserByUserId(String user_id) {
        //return userRepository.findByUserId(user_id);
    //}

    // num이 있는지 T or F
    public Optional<UserEntity> getUserByNum(Long num) {
        return userRepository.findById(num);
    }

    // email이 있는지 T or F
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // email로 찾아서 num 반환
    public Long getUserNumByEmail(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        // 유저가 존재하면 num 반환, 그렇지 않으면 null 반환
        return userOptional.map(UserEntity::getNum).orElse(null);

    }


    public boolean login(String email, String password) {
        // 이메일로 사용자 검색
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        // 사용자가 존재하고, 비밀번호가 일치하는지 확인
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return user.getUser_password().equals(password); // 암호화를 사용하지 않는 경우
        }

        return false;
    }
}