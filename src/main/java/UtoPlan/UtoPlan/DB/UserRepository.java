package UtoPlan.UtoPlan.DB;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //Optional<UserEntity> findByUserId(String user_id);

}
