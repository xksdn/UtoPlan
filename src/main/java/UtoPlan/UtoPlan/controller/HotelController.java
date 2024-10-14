//package UtoPlan.UtoPlan.controller;
//
//import UtoPlan.UtoPlan.Model.AmadeusService;
//import UtoPlan.UtoPlan.Model.HotelSearchRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Base64;
//import java.util.Map;
//
//@Slf4j
//@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com", "https://utoplan.framer.website/"}) // CORS 설정
//@RestController
//@RequestMapping("/api/hotels")
//public class HotelController {
//
//    @Autowired
//    private AmadeusService amadeusService;
//    //amadeusService.getAccessToken();
//
//    @PostMapping("/search")
//    public ResponseEntity<?> searchHotels(@RequestBody HotelSearchRequest request,
//                                          @RequestHeader(value = "Authorization", required = false) String authorization) {
//        // 1. Authorization 헤더 체크
//        if (authorization == null || !isValidAuthorization(authorization)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("success", false, "message", "Unauthorized"));
//        }
//
//        // 2. 요청값 처리
//        String cityCode = request.getLocation();
//        String checkInDate = request.getCheckInDate();  // 추가 로직에 따라 사용 가능
//        String checkOutDate = request.getCheckOutDate(); // 추가 로직에 따라 사용 가능
//        int adults = request.getAdults();               // 추가 로직에 따라 사용 가능
//        int radius = 1; // 기본 범위를 1km로 설정
//
//        log.info("Search Request - cityCode: {}, checkInDate: {}, checkOutDate: {}, adults: {}, radius: {}",
//                cityCode, checkInDate, checkOutDate, adults, radius);
//
//        try {
//            // 3. Amadeus 서비스로 요청
//            String response = amadeusService.searchHotelsByCity(cityCode, radius);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("Error during hotel search: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("success", false, "message", "Hotel search failed"));
//        }
//    }
//
//    // Authorization 검증 로직을 분리하여 명확하게
//    private boolean isValidAuthorization(String authorization) {
//        String expectedAuth = "Basic " + Base64.getEncoder().encodeToString("user:987456321".getBytes());
//        return authorization.equals(expectedAuth);
//    }
//
//}
//
//
//
