package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.LanguageResponse;
import ptit.com.enghub.mapper.LanguageMapper;
import ptit.com.enghub.repository.LanguageRepository;
import ptit.com.enghub.service.IService.LanguageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public List<LanguageResponse> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toResponse)
                .collect(Collectors.toList());
    }
}
