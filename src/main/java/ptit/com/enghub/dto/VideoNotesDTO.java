package ptit.com.enghub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoNotesDTO {
    private Long id;
    private Integer timestamp;
    private String content;
}
