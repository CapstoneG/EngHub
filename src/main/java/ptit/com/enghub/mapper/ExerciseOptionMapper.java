package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.response.ExerciseOptionResponse;
import ptit.com.enghub.entity.ExerciseOption;

@Mapper(componentModel = "spring")
public interface ExerciseOptionMapper {
    ExerciseOptionResponse toResponse(ExerciseOption option);
}
