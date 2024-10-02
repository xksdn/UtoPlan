package UtoPlan.UtoPlan.openAPI;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


// 여행 계획을 관리하는 객체
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayPlan {
    private LocalDate day;
    private List<Place> placeList;
}
