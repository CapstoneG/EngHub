package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.com.enghub.dto.request.VocabRequest;
import ptit.com.enghub.dto.response.VocabResponse;
import ptit.com.enghub.dto.response.VocabSimpleResponse;
import ptit.com.enghub.entity.Vocab;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VocabMapper {
    @Mapping(target = "topics", ignore = true) // xử lý trong service
    Vocab toEntity(VocabRequest request);

    @Mapping(target = "topics", ignore = true) // tránh vòng lặp
    VocabResponse toResponse(Vocab vocab);

    List<VocabResponse> toResponseList(List<Vocab> vocabs);

    VocabSimpleResponse toSimpleResponse(Vocab vocab);
}
