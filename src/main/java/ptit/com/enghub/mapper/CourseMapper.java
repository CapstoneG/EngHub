package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.response.CourseResponse;
import ptit.com.enghub.entity.Course;

@Mapper(componentModel = "spring", uses = {LanguageMapper.class, UnitMapper.class})
public interface CourseMapper {
    CourseResponse toResponse(Course course);
}
