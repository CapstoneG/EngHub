package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.response.CourseResponse;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseById(Long id);
}
