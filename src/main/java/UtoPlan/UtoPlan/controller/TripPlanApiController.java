package UtoPlan.UtoPlan.controller;


import UtoPlan.UtoPlan.DB.TripEntity;
import UtoPlan.UtoPlan.DB.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com"})
@RequestMapping("/api/trip")
public class TripPlanApiController {


    private final TripRepository tripRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveTrip(
            @RequestBody TripEntity tripEntity
            ) {
        tripRepository.save(tripEntity);
        return ResponseEntity.ok("Trip data saved successfully");
    }
}
