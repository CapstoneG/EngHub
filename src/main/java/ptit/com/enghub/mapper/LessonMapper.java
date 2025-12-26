package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.entity.Lesson;

@Mapper(componentModel = "spring", uses = {ExerciseMapper.class, DialogueMapper.class})
public interface LessonMapper {
    @Mapping(source = "dialogues", target = "dialogues")
    LessonResponse toResponse(Lesson lesson);
}
