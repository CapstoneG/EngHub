package ptit.com.enghub.service.IService;

import com.nimbusds.jose.JOSEException;
import ptit.com.enghub.dto.request.AuthenticationRequest;
import ptit.com.enghub.dto.request.IntrospectRequest;
import ptit.com.enghub.dto.request.RefreshTokenRequest;
import ptit.com.enghub.dto.request.UserCreationRequest;
import ptit.com.enghub.dto.response.AuthenticationResponse;
import ptit.com.enghub.dto.response.IntrospectResponse;
import ptit.com.enghub.dto.response.SessionResponse;
import ptit.com.enghub.entity.RefreshToken;
import ptit.com.enghub.entity.User;

import java.text.ParseException;
import java.util.List;

public interface AuthenticationService {
    public AuthenticationResponse register(UserCreationRequest request);
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public String generateToken(User user);
    public RefreshToken generateRefreshToken(User user);
    public AuthenticationResponse refreshToken(RefreshTokenRequest request);
    public void logout(RefreshTokenRequest request);
    public List<SessionResponse> getSessions(String email);
    public void revokeSession(Long sessionId);
    public void revokeAllSessions(String email);
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
