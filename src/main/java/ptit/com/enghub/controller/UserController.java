package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.com.enghub.dto.response.UserResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.repository.UserRepository;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String username = (String) authentication.getPrincipal(); // principal là String
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .level(user.getLevel())
                .streak(user.getStreak())
                .xp(user.getXp())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build());
    }

}
