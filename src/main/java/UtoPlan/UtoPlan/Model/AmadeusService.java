//package UtoPlan.UtoPlan.Model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class AmadeusService {
//
//    @Value("${amadeus.api.key}")
//    private String apiKey;
//
//    @Value("${amadeus.api.secret}")
//    private String apiSecret;
//
//    private final RestTemplate restTemplate;
//
//    public AmadeusService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String getAccessToken() {
//        String url = "https://test.api.amadeus.com/v1/security/oauth2/token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // Form data로 전송할 파라미터들
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("grant_type", "client_credentials");
//        formData.add("client_id", apiKey);
//        formData.add("client_secret", apiSecret);
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
//
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
//
//        // Access token이 포함된 응답을 파싱
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            return (String) response.getBody().get("access_token");
//        }
//
//        throw new RuntimeException("Failed to retrieve access token");
//    }
//
//
//    public String searchHotelsByCity(String cityCode, int radius) {
//        String accessToken = getAccessToken();
//        String url = "https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city"; // HTTPS로 변경
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("cityCode", cityCode)
//                .queryParam("radius", radius);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//
//        // 응답 상태 코드와 본문 확인
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response.getBody();
//        } else {
//            // 상태 코드와 본문을 로그에 출력하여 문제 진단
//            System.err.println("Failed to fetch hotel data from Amadeus API: " + response.getStatusCode() + ", " + response.getBody());
//            throw new RuntimeException("Failed to fetch hotel data from Amadeus API");
//        }
//    }
//
//
//}
//
//
