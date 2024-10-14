package UtoPlan.UtoPlan.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDTO {
    private Long id;
    private Integer day;
    private LocalDate date;
    private List<PlaceDTO> places;
}
