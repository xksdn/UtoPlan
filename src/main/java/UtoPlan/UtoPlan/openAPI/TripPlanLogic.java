package UtoPlan.UtoPlan.openAPI;

import UtoPlan.UtoPlan.DB.TripEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class TripPlanLogic {

    private GooglePlacesAPI googlePlacesAPI;

    public TripPlanLogic(GooglePlacesAPI googlePlacesAPI) {
        this.googlePlacesAPI = googlePlacesAPI;
    }

    public List<DayPlan> tripLogic (TripEntity tripEntity) throws JsonProcessingException {

        List<Place> places = googlePlacesAPI.searchPlaces(tripEntity);
        List<DayPlan> dayPlans = new ArrayList<>();

        Period period = Period.between(tripEntity.getStartDate(), tripEntity.getEndDate());

        int days = period.getDays() + 1;

        int placesPerDay = places.size() / days;
        int remainingPlaces = places.size() % days; // 남은 장소

        int placeIndex = 0;

        for (int i = 0; i < days; i++) {
            DayPlan dayPlan = new DayPlan();
            List<Place> dayPlaces = new ArrayList<>();

            for (int j = 0; j < placesPerDay; j++) {
                if (placeIndex < places.size()) {
                    dayPlaces.add(places.get(placeIndex));
                    placeIndex++;
                }
            }

            if (remainingPlaces > 0) {
                dayPlaces.add(places.get(placeIndex));
                placeIndex++;
                remainingPlaces--;
            }

            dayPlan.setPlaceList(dayPlaces);
            dayPlan.setDay(tripEntity.getStartDate().plusDays(i));
            dayPlans.add(dayPlan);
        }

        return dayPlans;
    }
}
