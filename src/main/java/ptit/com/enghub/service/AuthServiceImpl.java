package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.UserLoginRequest;
import ptit.com.enghub.dto.response.AuthResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.security.JwtUtil;
import ptit.com.enghub.service.IService.AuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
