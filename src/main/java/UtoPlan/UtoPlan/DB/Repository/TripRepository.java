package UtoPlan.UtoPlan.DB.Repository;

import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<TripEntity, Long> {
    List<TripEntity> findByUser(UserEntity user);

}

