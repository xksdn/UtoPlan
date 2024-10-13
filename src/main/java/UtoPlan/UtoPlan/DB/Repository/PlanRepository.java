package UtoPlan.UtoPlan.DB.Repository;

import UtoPlan.UtoPlan.DB.Entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
}
