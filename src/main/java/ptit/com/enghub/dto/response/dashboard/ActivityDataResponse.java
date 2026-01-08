package ptit.com.enghub.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDataResponse {

    private String name; // T2, T3, ...
    private int flashcards;
    private int lessons;
    private int skills;
}
