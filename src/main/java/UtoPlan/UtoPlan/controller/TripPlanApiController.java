package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import UtoPlan.UtoPlan.DB.Repository.TripRepository;
import UtoPlan.UtoPlan.openAPI.GoogleAPI.DayPlan;
import UtoPlan.UtoPlan.openAPI.GoogleAPI.TripPlanLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com", "https://utoplan.framer.website/"})
@RequestMapping("/api/trip")
public class TripPlanApiController {
    private final TripRepository tripRepository;
    private final TripPlanLogic tripPlanLogic;

    @PostMapping("/save")
    public ResponseEntity<String> saveTrip(
            @RequestBody TripEntity tripEntity
            ) {
        log.info("Received Inquiry: {}", tripEntity);
        tripRepository.save(tripEntity);
        return ResponseEntity.ok("Trip data saved successfully");
    }


    @PostMapping("/response")
    public ResponseEntity<String> getPlaces (
            @RequestBody TripRepository tripRepository
    ) {

        return ResponseEntity.ok("");
    }

//    @PostMapping("/plan")
//    public ResponseEntity<List<DayPlan>> sendPlan (
//            @RequestBody TripEntity tripEntity
//    ) throws JsonProcessingException {
//        log.info(" ", tripEntity);
//        List<DayPlan> dayPlans = tripPlanLogic.tripLogic(tripEntity);
//        return ResponseEntity.ok(dayPlans);
//    }

    @PostMapping("/plan")
    public ResponseEntity<List<DayPlan>> sendPlan () throws JsonProcessingException {
        Long testUserId = 2001L;
        TripEntity tripEntity = tripRepository.findById(testUserId)
                        .orElseThrow(() -> new RuntimeException("Test user not found"));

        log.info(" ", tripEntity);
        List<DayPlan> dayPlans = tripPlanLogic.tripLogic(tripEntity);
        return ResponseEntity.ok(dayPlans);
    }


}
