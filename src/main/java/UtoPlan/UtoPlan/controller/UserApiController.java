package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.DB.UserEntity;
import UtoPlan.UtoPlan.DB.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserRepository userRepository;

    @GetMapping("/find-all")
    public List<UserEntity> findAll () {
        return userRepository.findAll();
    }



//    @GetMapping("/name")
//    public void autoSave(
//            @RequestParam String name,
//            @RequestParam String password,
//            @RequestParam String email
//    ) {
//        var user = UserEntity.builder()
//                .username(name)
//                .user_password(password)
//                .email(email)
//                .build();
//
//        userRepository.save(user);
//    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(
            @RequestBody UserEntity userEntity
    ) {
        userRepository.save(userEntity);
        return ResponseEntity.ok("User data saved successfully");
    }
}
