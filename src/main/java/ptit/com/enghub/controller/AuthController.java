package ptit.com.enghub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.ResponseDto;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        ResponseDto response = new ResponseDto(
                "200",
                "User registered successfully",
                Map.of("username", user.getUsername(), "role", user.getRole())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );

        User user = userRepository.findByUsername(request.get("username"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return Map.of("token", token);
    }
}
