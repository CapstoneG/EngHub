package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.ResponseDto;
import ptit.com.enghub.dto.request.UserCreateRequest;
import ptit.com.enghub.dto.request.UserLoginRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.AuthResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.security.JwtUtil;
import ptit.com.enghub.service.IService.AuthService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

     private final AuthenticationManager authenticationManager;
     private final JwtUtil jwtUtil;
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody UserCreateRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

         if (userRepository.findByUsername(user.getUsername()).isPresent()) {
             ResponseDto response = new ResponseDto(
                     "400",
                     "Username already exists",
                     null
             );
             return ResponseEntity.badRequest().body(response);
         }
        userRepository.save(user);

        ResponseDto response = new ResponseDto(
                "200",
                "User registered successfully",
                Map.of("username", user.getUsername(), "role", user.getRole())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody UserLoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}
