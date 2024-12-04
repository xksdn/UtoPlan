package UtoPlan.UtoPlan.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelSearchRequest {
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private int adults;
}

