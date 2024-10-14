package UtoPlan.UtoPlan.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;
    private String imageUrl;
}
