package ptit.com.enghub.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ptit.com.enghub.dto.request.AuthenticationRequest;
import ptit.com.enghub.dto.request.IntrospectRequest;
import ptit.com.enghub.dto.request.RefreshTokenRequest;
import ptit.com.enghub.dto.request.UserCreationRequest;
import ptit.com.enghub.dto.response.AuthenticationResponse;
import ptit.com.enghub.dto.response.IntrospectResponse;
import ptit.com.enghub.dto.response.SessionResponse;
import ptit.com.enghub.entity.RefreshToken;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserLearningSettings;
import ptit.com.enghub.enums.EnumRole;
import ptit.com.enghub.enums.UserStatus;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.mapper.UserMapper;
import ptit.com.enghub.repository.RefreshTokenRepository;
import ptit.com.enghub.repository.RoleRepository;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.repository.UserSettingsRepository;
import ptit.com.enghub.service.IService.AuthenticationService;
import ptit.com.enghub.service.IService.VerificationTokenService;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserMapper userMapper;
    VerificationTokenService verificationTokenService;
    UserSettingsRepository settingsRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (!user.isVerified()){
            return AuthenticationResponse.builder()
                    .authenticated(false)
                    .verified(false)
                    .message("Tài khoản đã tồn tại nhưng chưa xác thực. Vui lòng xác thực email.")
                    .build();
        }

        if ( !user.getStatus().equals(UserStatus.ACTIVE) ){
            return AuthenticationResponse.builder()
                    .authenticated(false)
                    .verified(false)
                    .message("Tài khoản không hoạt động")
                    .build();
        }

        var accessToken = generateToken(user);
        var refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken.getToken())
                .authenticated(true)
                .verified(user.isVerified())
                .build();
    }

    @Override
    public AuthenticationResponse register(UserCreationRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setProvider("LOCAL");
        user.setVerified(false);

        var roles = roleRepository.findByName(EnumRole.USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.getRoles().add(roles);

        User savedUser = userRepository.save(user);


        UserLearningSettings settings = UserLearningSettings.builder()
                .user(user)
                .dailyStudyReminder(true)
                .reminderTime(LocalTime.of(21, 0))
                .emailNotification(true)
                .dailyStudyMinutes(15)
                .targetDaysPerWeek(5)
                .build();

        settingsRepository.save(settings);

        verificationTokenService.createAndSendVerificationToken(savedUser);

        return AuthenticationResponse.builder()
                .authenticated(false)
                .verified(false)
                .message("Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.")
                .build();
    }

    public String generateToken(User user) {

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("tu.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(12, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing token", e);
        }
    }

    public RefreshToken generateRefreshToken(User user) {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String refreshToken = Base64.getUrlEncoder().encodeToString(randomBytes);

        Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        String deviceInfo = userAgent != null ? userAgent + " (IP: " + ipAddress + ")" : "Unknown Device";

        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(expiresAt)
                .status("active")
                .deviceInfo(deviceInfo) // Lưu deviceInfo
                .build();

        return refreshTokenRepository.save(tokenEntity);
    }

    // Làm mới access token
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!"active".equals(refreshToken.getStatus()) || refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        var user = refreshToken.getUser();
        var newAccessToken = generateToken(user);

        return AuthenticationResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .authenticated(true)
                .build();
    }

    // Đăng xuất
    public void logout(RefreshTokenRequest request) {
        var refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        refreshToken.setStatus("revoked");
        refreshTokenRepository.save(refreshToken);
    }

    // Liệt kê các phiên đăng nhập
    public List<SessionResponse> getSessions(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return refreshTokenRepository.findAllByUser(user).stream()
                .filter(token -> "active".equals(token.getStatus()))
                .map(token -> SessionResponse.builder()
                        .id(token.getId())
                        .deviceInfo(token.getDeviceInfo())
                        .expiresAt(token.getExpiresAt())
                        .build())
                .collect(Collectors.toList());
    }

    // Xóa phiên đăng nhập cụ thể
    public void revokeSession(Long sessionId) {
        var refreshToken = refreshTokenRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        refreshToken.setStatus("revoked");
        refreshTokenRepository.save(refreshToken);
    }

    // Xóa tất cả phiên đăng nhập
    public void revokeAllSessions(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        refreshTokenRepository.findAllByUser(user).forEach(token -> {
            token.setStatus("revoked");
            refreshTokenRepository.save(token);
        });
    }

    // Introspect (giữ nguyên)
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var verified = signedJWT.verify(verifier);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    // Build scope (giữ nguyên)
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getName().name()));
        }
        return stringJoiner.toString();
    }
}