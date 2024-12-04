package UtoPlan.UtoPlan.openAPI.GoogleAPI;

import UtoPlan.UtoPlan.DB.Entity.TripEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GooglePlacesAPI {

    // AIzaSyA2c2TPzPS8qDDZsBTpCxS7wlT77dWT5F8
    private final String GoogleAPIKey = "AIzaSyA2c2TPzPS8qDDZsBTpCxS7wlT77dWT5F8";


    public List<Place> searchPlaces(TripEntity tripEntity) throws JsonProcessingException {
        // 여행 장소 및 스타일을 조합하여 검색 쿼리 작성
        String placeName = tripEntity.getTripCity(); // 여행 장소
        String tripStyles = tripEntity.getTripStyle(); // 여행 스타일

        String[] styles = tripStyles.split(",");
        List<Place> allPlaces = new ArrayList<>();

        for (String style : styles) {
            String query = String.format("%s %s", placeName, style.trim());

            double[] latLng = getLatLngFromLocationName(placeName);
            double lat = latLng[0];
            double lng = latLng[1];
            int radius = 10000; // 10km 반경

            String apiurl = String.format(
                    "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s&location=%f,%f&radius=%d&key=%s&language=ko",
                    query, lat, lng, radius, GoogleAPIKey
            );

            log.info("Calling Google Places API with URL: {}", apiurl);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiurl, String.class);

            List<Place> places = parsePlaces(response.getBody());
            log.info("Places found for style '{}': {}", style, places.size());
            allPlaces.addAll(places); // 모든 검색 결과를 통합
        }

        log.info("Total number of places found: {}", allPlaces.size());
        return allPlaces;
    }








    // 장소 이름으로 위도/경도를 가져오는 메서드
    private double[] getLatLngFromLocationName(String placeName) throws JsonProcessingException {
        String apiurl = String.format(
                "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s&key=%s",
                placeName, GoogleAPIKey
        );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiurl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode location = root.path("results").get(0).path("geometry").path("location");

        double lat = location.path("lat").asDouble();
        double lng = location.path("lng").asDouble();

        return new double[]{lat, lng};
    }


    private List<Place> parsePlaces(String responseBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        JsonNode results = root.path("results");

        List<Place> places = new ArrayList<>();

        for (JsonNode result : results) {
            String photoReference = "";

            // photos 정보가 있는지 확인하고, 있다면 가져옵니다.
            if (result.has("photos") && result.path("photos").isArray() && result.path("photos").size() > 0) {
                photoReference = result.path("photos").get(0).path("photo_reference").asText();
            }

            // 만약 photoReference가 없으면 장소를 추가하지 않고 다음으로 진행합니다.
            if (photoReference.isEmpty()) {
                log.info("Skipping place '{}' because it has no photos.", result.path("name").asText());
                continue;
            }

            // 사진이 있는 경우에만 이미지 URL 생성
            String imageUrl = String.format(
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                    photoReference, GoogleAPIKey);

            // details API 호출을 통해 전화번호 정보를 가져옵니다.
            String placeId = result.path("place_id").asText();
            String phoneNumber = getPhoneNumberFromPlaceId(placeId);

            JsonNode typesNode = result.path("types");
            String timeSlot = determineTimeSlot(typesNode);

            // 장소 객체를 생성하여 리스트에 추가
            Place place = Place.builder()
                    .name(result.path("name").asText())
                    .lat(result.path("geometry").path("location").path("lat").asDouble())
                    .lng(result.path("geometry").path("location").path("lng").asDouble())
                    .address(result.path("formatted_address").asText())
                    .rating(result.has("rating") ? result.path("rating").asDouble() : 0.0) // rating 정보가 없을 때 기본 값 처리
                    .image(imageUrl)
                    .phone(phoneNumber)
                    .timeSlot(timeSlot)
                    .build();

            // 사진이 있는 장소만 리스트에 추가
            log.info("Parsed place: {}", place);
            places.add(place);
        }
        log.info("Total places parsed: {}", places.size());
        return places;
    }

    // 유형 정보를 바탕으로 시간대 결정
    private String determineTimeSlot(JsonNode typesNode) {
        if (typesNode.isArray()) {
            for (JsonNode type : typesNode) {
                String typeValue = type.asText().toLowerCase();
                if (typeValue.contains("park") || typeValue.contains("family")) {
                    return "morning";
                } else if (typeValue.contains("museum") || typeValue.contains("tourist")) {
                    return "afternoon";
                } else if (typeValue.contains("restaurant") || typeValue.contains("night")) {
                    return "evening";
                }
            }
        }
        return "afternoon"; // 기본값
    }

    // placeId를 이용하여 전화번호를 가져오는 메서드 추가
    private String getPhoneNumberFromPlaceId(String placeId) {
        String apiurl = String.format(
                "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=formatted_phone_number&key=%s",
                placeId, GoogleAPIKey
        );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiurl, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode phoneNode = root.path("result").path("formatted_phone_number");

            return phoneNode.asText("");
        } catch (JsonProcessingException e) {
            log.error("Error parsing phone number from place details: ", e);
            return "";
        }
    }

}
