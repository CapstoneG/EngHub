package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.com.enghub.dto.response.AchievementProgressResponse;
import ptit.com.enghub.entity.Achievement;
import ptit.com.enghub.entity.AchievementProgress;

@Mapper(componentModel = "spring")
public interface AchievementProgressMapper {

    @Mapping(target = "achievementId", source = "achievement.id")
    @Mapping(target = "achievementName", source = "achievement.name")
    @Mapping(target = "type", source = "achievement.type")
    @Mapping(
            target = "currentValue",
            expression = "java(progress != null ? progress.getCurrentValue() : 0)"
    )
    @Mapping(
            target = "targetValue",
            source = "achievement.conditionValue"
    )
    @Mapping(
            target = "iconUrl",
            source = "achievement.iconUrl"
    )
    @Mapping(
            target = "completed",
            expression = "java(progress != null && progress.getCurrentValue() >= achievement.getConditionValue())"
    )
    AchievementProgressResponse toResponse(
            Achievement achievement,
            AchievementProgress progress
    );
}
