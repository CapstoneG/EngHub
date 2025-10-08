package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.UserCreateRequest;
import ptit.com.enghub.dto.request.UserLoginRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.AuthResponse;
import ptit.com.enghub.dto.response.UserResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.enums.Role;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.service.IService.AuthService;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserCreateRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .role(Role.USER)
                .streak(1)
                .createdAt(LocalDateTime.now())
                .build();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
             return ResponseEntity.badRequest().body(
                     ApiResponse.error(400, "Username already exists")
             );
        }
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success(UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody UserLoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}
