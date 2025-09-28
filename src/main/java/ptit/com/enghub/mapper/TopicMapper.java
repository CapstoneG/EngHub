package ptit.com.enghub.mapper;

import org.mapstruct.*;
import ptit.com.enghub.dto.request.TopicRequest;
import ptit.com.enghub.dto.response.TopicResponse;
import ptit.com.enghub.dto.response.TopicSimpleResponse;
import ptit.com.enghub.entity.Topic;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(target = "vocabs", ignore = true) // xử lý trong service
    Topic toEntity(TopicRequest request);

    @Mapping(target = "vocabs", ignore = true) // tránh vòng lặp
    TopicResponse toResponse(Topic topic);

    List<TopicResponse> toResponseList(List<Topic> topics);

    TopicSimpleResponse toSimpleResponse(Topic topic);
}
