package UtoPlan.UtoPlan.DB;


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
@Builder
@Entity(name = "inquiry")  // 테이블 이름
public class InquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @Column(name = "q_name")  // 'title'로 전송된 데이터 매핑
    private String title;

    @Column(name = "q_detail")  // 'content'로 전송된 데이터 매핑
    private String content;
}


