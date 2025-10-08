package ptit.com.enghub.service.IService;


import ptit.com.enghub.dto.response.LanguageResponse;

import java.util.List;

public interface LanguageService {
    List<LanguageResponse> getAllLanguages();
}
