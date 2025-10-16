package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ptit.com.enghub.dto.request.CourseRequest;
import ptit.com.enghub.dto.response.CourseResponse;
import ptit.com.enghub.entity.Course;
import ptit.com.enghub.entity.Language;

@Mapper(componentModel = "spring", uses = {LanguageMapper.class, UnitMapper.class})
public interface CourseMapper {
    CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "units", ignore = true)
    Course toEntity(CourseRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "units", ignore = true)
    void updateCourseFromRequest(CourseRequest request, @MappingTarget Course course);
}
