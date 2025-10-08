package ptit.com.enghub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SessionResponse {
    private Long id;
    private String deviceInfo;
    private Instant expiresAt;
}
