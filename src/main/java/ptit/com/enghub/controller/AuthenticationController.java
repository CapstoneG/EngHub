package ptit.com.enghub.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ptit.com.enghub.dto.request.AuthenticationRequest;
import ptit.com.enghub.dto.request.IntrospectRequest;
import ptit.com.enghub.dto.request.RefreshTokenRequest;
import ptit.com.enghub.dto.request.UserCreationRequest;
import ptit.com.enghub.dto.response.*;
import ptit.com.enghub.service.GoogleAuthService;
import ptit.com.enghub.service.IService.AuthenticationService;
import ptit.com.enghub.service.IService.VerificationTokenService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    GoogleAuthService googleAuthService;
    VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@RequestBody @Valid UserCreationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                        .result(authenticationService.register(request))
                        .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequest request) {
        authenticationService.logout(request);
    }

    @GetMapping("/sessions")
    public List<SessionResponse> getSessions(@RequestParam String email) {
        return authenticationService.getSessions(email);
    }

    @PostMapping("/sessions/revoke")
    public void revokeSession(@RequestParam Long sessionId) {
        authenticationService.revokeSession(sessionId);
    }

    @PostMapping("/sessions/revoke-all")
    public void revokeAllSessions(@RequestParam String email) {
        authenticationService.revokeAllSessions(email);
    }

    @PostMapping("/google")
    public AuthenticationResponse googleLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        return googleAuthService.loginWithGoogle(code);
    }

    @GetMapping("/verify")
    public ModelAndView verifyEmail(@RequestParam String token) {
        boolean isVerified = verificationTokenService.verifyToken(token);
        ModelAndView mav = new ModelAndView("verify"); // tên template
        mav.addObject("message", isVerified ? "Email verified successfully!" : "Invalid or expired token.");
        return mav;
    }

}
