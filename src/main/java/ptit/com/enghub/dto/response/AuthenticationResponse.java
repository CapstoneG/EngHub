package ptit.com.enghub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    String refreshToken;
    boolean authenticated;
    private boolean newlyRegistered;
    private boolean verified;        // <-- để FE biết trạng thái verify
    private String message;
}
