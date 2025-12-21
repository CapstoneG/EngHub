package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.UserLearningSettingsDto;
import ptit.com.enghub.dto.request.ChangePasswordRequest;
import ptit.com.enghub.dto.request.UserUpdateRequest;
import ptit.com.enghub.dto.response.UserResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserLearningSettings;
import ptit.com.enghub.enums.UserStatus;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.mapper.UserMapper;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.repository.UserSettingsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSettingsRepository userSettingsRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getYourInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        User user = getCurrentUser();
        userMapper.updateUser(user, request);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUser(Long userId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        userRepository.deleteById(userId);
        return "User was deleted";
    }

    public void updateUserStatus(UserStatus status) {
        User user = getCurrentUser();
        user.setStatus(status);
        userRepository.save(user);
    }

    public UserLearningSettingsDto updateSettings(UserLearningSettingsDto request) {
        User user = getCurrentUser();
        UserLearningSettings settings = userSettingsRepository
                .findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.SETTINGS_NOT_FOUND));

        if (request.getDailyStudyReminder() != null) {
            settings.setDailyStudyReminder(request.getDailyStudyReminder());
        }

        if (request.getReminderTime() != null) {
            settings.setReminderTime(request.getReminderTime());
        }

        if (request.getEmailNotification() != null) {
            settings.setEmailNotification(request.getEmailNotification());
        }

        if (request.getDailyStudyMinutes() != null) {
            settings.setDailyStudyMinutes(request.getDailyStudyMinutes());
        }

        if (request.getTargetDaysPerWeek() != null) {
            settings.setTargetDaysPerWeek(request.getTargetDaysPerWeek());
        }

        userSettingsRepository.save(settings);

        return new UserLearningSettingsDto(
                settings.isDailyStudyReminder(),
                settings.getReminderTime(),
                settings.isEmailNotification(),
                settings.getDailyStudyMinutes(),
                settings.getTargetDaysPerWeek()
        );
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}