package ptit.com.enghub.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.UserCreationRequest;
import ptit.com.enghub.dto.request.UserUpdateRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.UserResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.enums.Level;
import ptit.com.enghub.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;



    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getYourInfo(){
        return ResponseEntity.ok(userService.getYourInfo());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser(userId));
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request){
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PostMapping("/update-level")
    public ResponseEntity<UserResponse> updateLevelUser(@RequestBody Map<String, String> body){
        User user = userService.getUser();

        String levelStr = body.get("level");
        Level newLevel = Level.valueOf(levelStr.trim().toUpperCase());

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setLevel(newLevel);
        userUpdateRequest.setPassword(user.getPassword());
        userUpdateRequest.setFirstName(user.getFirstName());
        userUpdateRequest.setLastName(user.getLastName());
        userUpdateRequest.setPhoneNo(user.getProvider());
        log.info(userUpdateRequest.toString());
        return ResponseEntity.ok(userService.updateUser(user.getId(), userUpdateRequest));
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
