package UtoPlan.UtoPlan.DB.Repository;

import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<TripEntity, Long> {
    List<TripEntity> findByUser(UserEntity user);
    Optional<TripEntity> findByUserNum(Long userNum);

}