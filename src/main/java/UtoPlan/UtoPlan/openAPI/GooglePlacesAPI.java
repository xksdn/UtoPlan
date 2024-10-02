package UtoPlan.UtoPlan.openAPI;

import UtoPlan.UtoPlan.DB.TripEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GooglePlacesAPI {

    private final String GoogleAPIKey = "";


    public List<Place> searchPlaces(TripEntity tripEntity) throws JsonProcessingException {
        // 유저가 입력한 장소 이름으로 구글 api 호출
        String placeName = tripEntity.getTripPlace(); // 여행 장소
        String resultQuery = tripEntity.getTripStyle(); // 여행 스타일

        double[] latLng = getLatLngFromLocationName(placeName);
        double lat = latLng[0];
        double lng = latLng[1];
        int radius = 10000; // 10km 반경

        String apiurl = String.format(
                "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s&location=%f,%f&radius=%d&key=%s",
                resultQuery, lat, lng, radius, GoogleAPIKey
        );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiurl, String.class);

        List<Place> places = parsePlaces(response.getBody());

        return places;
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


    private List<Place> parsePlaces (String responseBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        JsonNode results = root.path("results");

        List<Place> places = new ArrayList<>();

        for (JsonNode result : results) {
            Place place = Place.builder()
                    .name(result.path("name").asText())
                    .lat(result.path("geometry").path("location").path("lat").asDouble())
                    .lng(result.path("geometry").path("location").path("lng").asDouble())
                    .address(result.path("formatted_address").asText())
                    .rating(result.path("rating").asDouble())
                    .build();

            places.add(place);
        }
        return places;
    }

}
