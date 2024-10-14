package UtoPlan.UtoPlan.DB.Repository;

import UtoPlan.UtoPlan.DB.Entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    List<PlanEntity> findByUser_Num(Long userId);
}
