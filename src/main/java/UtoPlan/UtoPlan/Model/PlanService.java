package UtoPlan.UtoPlan.Model;


import UtoPlan.UtoPlan.DB.Entity.PlaceEntity;
import UtoPlan.UtoPlan.DB.Entity.PlanEntity;
import UtoPlan.UtoPlan.DB.Repository.PlaceRepository;
import UtoPlan.UtoPlan.DB.Repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlaceRepository placeRepository;

    public void savePlanWithPlaces(List<PlanEntity> plans) {
        for (PlanEntity plan : plans) {
            PlanEntity savedPlan = planRepository.save(plan);
            if (plan.getPlaces() != null) {
                for (PlaceEntity place : plan.getPlaces()) {
                    place.setPlan(savedPlan);
                    placeRepository.save(place);
                }
            }
        }
    }
}
