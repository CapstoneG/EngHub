package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ptit.com.enghub.dto.response.AuthenticationResponse;
import ptit.com.enghub.entity.OAuth2Account;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.enums.EnumRole;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.repository.OAuth2AccountRepository;
import ptit.com.enghub.repository.RoleRepository;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.service.IService.AuthenticationService;
import ptit.com.enghub.service.IService.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final OAuth2AccountRepository oauth2AccountRepository;
    private final AuthenticationService authenticationService;
    private final VerificationTokenService verificationTokenService;
    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public AuthenticationResponse loginWithGoogle(String code) {
        String accessToken = getAccessToken(code);

        Map<String, Object> userInfo = getUserInfo(accessToken);
        String email = (String) userInfo.get("email");
        String firstName = (String) userInfo.get("given_name");
        String lastName = (String) userInfo.get("family_name");
        String providerId = (String) userInfo.get("sub");

        log.info("Google user info: {}", userInfo);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email, firstName, lastName, providerId));
        String jwt = authenticationService.generateToken(user);
        String refresh = authenticationService.generateRefreshToken(user).getToken();

        log.info(jwt);

        return AuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refresh)
                .authenticated(true)
                .verified(true)
                .newlyRegistered(false)
                .message("Đăng nhập thành công!")
                .build();
    }

    private String getAccessToken(String code) {
        String tokenUri = "https://oauth2.googleapis.com/token";
        Map<String, String> tokenRequest = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code"
        );

        Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUri, tokenRequest, Map.class);
        if (tokenResponse == null || tokenResponse.get("access_token") == null) {
            throw new RuntimeException("Failed to retrieve access token from Google");
        }
        return (String) tokenResponse.get("access_token");
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUri = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        Map<String, Object> userInfo = restTemplate.getForObject(userInfoUri, Map.class);
        if (userInfo == null) {
            throw new RuntimeException("Failed to retrieve user info from Google");
        }
        return userInfo;
    }

    private User createNewUser(String email, String firstName, String lastName, String providerId) {
        User newUser = User.builder()
                .email(email)
                .password("")
                .firstName(firstName)
                .lastName(lastName)
                .verified(true)
                .build();
        newUser.setCreatedAt(LocalDateTime.now());

        var roles = roleRepository.findByName(EnumRole.USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        newUser.getRoles().add(roles);

        User savedUser = userRepository.save(newUser);

        // Lưu thông tin OAuth2
        OAuth2Account oauth2Account = OAuth2Account.builder()
                .provider("GOOGLE")
                .providerId(providerId)
                .user(savedUser)
                .build();
        oauth2AccountRepository.save(oauth2Account);
        return savedUser;
    }
}
