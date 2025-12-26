package ptit.com.enghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StudyChartResponse {
    private String day;
    private int minutes;
}
