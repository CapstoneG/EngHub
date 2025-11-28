package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.entity.Lesson;

@Mapper(componentModel = "spring", uses = {ExerciseMapper.class})
public interface LessonMapper {
    LessonResponse toResponse(Lesson lesson);
}
