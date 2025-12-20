package ptit.com.enghub.mapper;

import org.mapstruct.*;
import ptit.com.enghub.dto.UserLearningSettingsDto;
import ptit.com.enghub.dto.request.UserCreationRequest;
import ptit.com.enghub.dto.request.UserUpdateRequest;
import ptit.com.enghub.dto.response.UserResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserLearningSettings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(source = "learningSettings", target = "learningSettings")
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default UserLearningSettingsDto toLearningSettingsResponse(
            UserLearningSettings settings) {
        if (settings == null) return null;

        return new UserLearningSettingsDto(
                settings.isDailyStudyReminder(),
                settings.getReminderTime(),
                settings.isEmailNotification(),
                settings.getDailyStudyMinutes(),
                settings.getTargetDaysPerWeek()
        );
    }
}
