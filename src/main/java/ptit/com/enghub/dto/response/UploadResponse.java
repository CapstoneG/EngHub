package ptit.com.enghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UploadResponse {
    private String url;
    private String publicId;
}
