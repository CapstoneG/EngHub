package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.response.UnitResponse;
import ptit.com.enghub.entity.Unit;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface UnitMapper {
    UnitResponse toResponse(Unit unit);
}
