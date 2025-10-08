package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.response.LanguageResponse;
import ptit.com.enghub.entity.Language;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    LanguageResponse toResponse(Language language);
}
