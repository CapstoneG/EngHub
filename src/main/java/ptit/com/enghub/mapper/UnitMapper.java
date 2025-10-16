package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ptit.com.enghub.dto.request.UnitRequest;
import ptit.com.enghub.dto.response.UnitResponse;
import ptit.com.enghub.entity.Unit;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface UnitMapper {
    UnitResponse toResponse(Unit unit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true) // gán thủ công trong service
    @Mapping(target = "lessons", ignore = true)
    Unit toEntity(UnitRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    void updateUnitFromRequest(UnitRequest request, @MappingTarget Unit unit);

}
