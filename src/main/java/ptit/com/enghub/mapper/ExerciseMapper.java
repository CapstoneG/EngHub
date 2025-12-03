package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.response.ExerciseResponse;
import ptit.com.enghub.entity.Exercise;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseResponse toResponse(Exercise exercise);
}
