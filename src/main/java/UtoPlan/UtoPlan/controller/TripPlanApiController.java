package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.DB.TripEntity;
import UtoPlan.UtoPlan.DB.TripRepository;
import UtoPlan.UtoPlan.openAPI.DayPlan;
import UtoPlan.UtoPlan.openAPI.GooglePlacesAPI;
import UtoPlan.UtoPlan.openAPI.TripPlanLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com"})
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

    @PostMapping("/plan")
    public ResponseEntity<List<DayPlan>> sendPlan (
            @RequestBody TripEntity tripEntity
    ) throws JsonProcessingException {
        log.info(" ", tripEntity);
        List<DayPlan> dayPlans = tripPlanLogic.tripLogic(tripEntity);
        return ResponseEntity.ok(dayPlans);
    }
}
