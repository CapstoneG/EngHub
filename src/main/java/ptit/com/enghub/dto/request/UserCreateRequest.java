package ptit.com.enghub.dto.request;

import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String email;
    private String username;
    private String password;
}
