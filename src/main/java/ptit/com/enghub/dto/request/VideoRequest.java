package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class VideoRequest {
    private String url;
    private String description;
    private Integer duration;
}