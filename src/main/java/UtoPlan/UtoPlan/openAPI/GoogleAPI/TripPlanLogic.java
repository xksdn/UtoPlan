package UtoPlan.UtoPlan.openAPI.GoogleAPI;

import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TripPlanLogic {

    private GooglePlacesAPI googlePlacesAPI;

    public TripPlanLogic(GooglePlacesAPI googlePlacesAPI) {
        this.googlePlacesAPI = googlePlacesAPI;
    }

    public List<DayPlan> tripLogic(TripEntity tripEntity) throws JsonProcessingException {

        List<Place> places = googlePlacesAPI.searchPlaces(tripEntity);

        List<Place> morningPlaces = new ArrayList<>();
        List<Place> afternoonPlaces = new ArrayList<>();
        List<Place> eveningPlaces = new ArrayList<>();

        for (Place place : places) {
            log.info("Time slot for place '{}': {}", place.getName(), place.getTimeSlot());
            if (isSuitableForMorning(place)) {
                morningPlaces.add(place);
                log.info("Place '{}' added to morningPlaces.", place.getName());
            } else if (isSuitableForAfternoon(place)) {
                afternoonPlaces.add(place);
                log.info("Place '{}' added to afternoonPlaces.", place.getName());
            } else if (isSuitableForEvening(place)) {
                eveningPlaces.add(place);
                log.info("Place '{}' added to eveningPlaces.", place.getName());
            } else {
                // 조건에 맞지 않는 경우 임의의 시간대로 추가 (예: 기본적으로 저녁으로 처리)
                eveningPlaces.add(place);
                log.info("Place '{}' added to eveningPlaces by default.", place.getName());
            }
        }


        log.info("Morning places: {}", morningPlaces);
        log.info("Afternoon places: {}", afternoonPlaces);
        log.info("Evening places: {}", eveningPlaces);


        List<DayPlan> dayPlans = new ArrayList<>();
        Period period = Period.between(tripEntity.getStartDate(), tripEntity.getEndDate());
        int days = period.getDays() + 1;

        for (int i = 0; i < days; i++) {
            DayPlan dayPlan = new DayPlan();

            dayPlan.setDay(tripEntity.getStartDate().plusDays(i));

            // 아침, 오후, 저녁 장소들을 적절하게 할당하여 병합
            List<Place> mergedPlaceList = new ArrayList<>();
            mergedPlaceList.addAll(assignPlaces(morningPlaces, 2));
            mergedPlaceList.addAll(assignPlaces(afternoonPlaces, 2));
            mergedPlaceList.addAll(assignPlaces(eveningPlaces, 2));

            log.info("Merged place list for day {}: {}", tripEntity.getStartDate().plusDays(i), mergedPlaceList);

            dayPlan.setPlaceList(mergedPlaceList);

            dayPlans.add(dayPlan);
        }

        log.info("Final day plans: {}", dayPlans);
        return dayPlans;
    }

    private boolean isSuitableForMorning(Place place) {
        String type = place.getTimeSlot().toLowerCase();
        // 기존의 "park", "family", "cafe"에 추가적으로 아침 활동에 적합한 장소 타입을 확장
        return type.contains("park") || type.contains("family") || type.contains("cafe") ||
                type.contains("breakfast") || type.contains("morning") || type.contains("trail");
    }

    private boolean isSuitableForAfternoon(Place place) {
        String type = place.getTimeSlot().toLowerCase();
        // 기존의 조건 외에 추가적인 타입 확장
        return type.contains("museum") || type.contains("tourist") || type.contains("sports") ||
                type.contains("lunch") || type.contains("afternoon");
    }

    private boolean isSuitableForEvening(Place place) {
        String type = place.getTimeSlot().toLowerCase();
        // 저녁 타입은 기존 조건을 유지하면서 더 넓은 범위를 수용할 수 있습니다.
        return type.contains("restaurant") || type.contains("night") ||
                type.contains("bar") || type.contains("dinner") || type.contains("evening");
    }


    private List<Place> assignPlaces(List<Place> places, int numPlaces) {
        List<Place> assignedPlaces = new ArrayList<>();
        for (int i = 0; i < numPlaces && !places.isEmpty(); i++) {
            assignedPlaces.add(places.remove(0));
        }
        return assignedPlaces;
    }
}