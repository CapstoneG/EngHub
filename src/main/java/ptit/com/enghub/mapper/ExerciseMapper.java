package ptit.com.enghub.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ptit.com.enghub.dto.ExerciseDTO;
import ptit.com.enghub.entity.Exercise;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    @Mapping(source = "metadata", target = "metadata", qualifiedByName = "toJson")
    ExerciseDTO toDTO(Exercise exercise);

    @Named("toJson")
    default JsonNode toJson(String metadata) {
        try {
            return new ObjectMapper().readTree(metadata);
        } catch (Exception e) {
            return null;
        }
    }
}

