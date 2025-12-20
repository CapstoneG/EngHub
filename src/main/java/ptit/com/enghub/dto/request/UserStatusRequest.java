package ptit.com.enghub.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ptit.com.enghub.enums.UserStatus;

@Data
public class UserStatusRequest {
    @NotNull
    UserStatus status;
}
