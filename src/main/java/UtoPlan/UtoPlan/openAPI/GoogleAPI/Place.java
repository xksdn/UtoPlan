package UtoPlan.UtoPlan.openAPI.GoogleAPI;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// google places api에서 받아온 정보를 저장해두는 객체
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Place {
    private String name;
    private double lat;
    private double lng;
    private String address;
    private double rating;
}
