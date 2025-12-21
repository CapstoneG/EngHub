package ptit.com.enghub.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ptit.com.enghub.dto.UserLearningSettingsDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String email;
    String firstName;
    String lastName;
    String avatarUrl;
    Set<RoleResponse> roles;
    String level;
    String status;
    String provider;
    boolean verified;
    LocalDateTime lastLogin;
    LocalDateTime createdAt;
    UserLearningSettingsDto learningSettings;
}

