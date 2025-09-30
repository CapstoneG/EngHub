package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.request.UserLoginRequest;
import ptit.com.enghub.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(UserLoginRequest request);
}
