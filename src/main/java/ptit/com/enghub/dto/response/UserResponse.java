package ptit.com.enghub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private String email;
    private String avatar;
    private String level;
    private int streak;
    private int xp;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
