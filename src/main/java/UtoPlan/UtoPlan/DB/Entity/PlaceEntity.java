package UtoPlan.UtoPlan.DB.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity(name = "place")
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double latitude;
    private double longitude;

    @JsonProperty("imageUrl")
    @Column(name = "image_url", length = 500) // 또는 columnDefinition = "TEXT"
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private PlanEntity plan;

}
