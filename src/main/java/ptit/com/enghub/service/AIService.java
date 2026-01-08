package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ptit.com.enghub.dto.request.ExternalDictionaryRequest;
import ptit.com.enghub.dto.response.BulkFlashcardResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String DICT_API_URL =
            "https://external-api.com/dictionary/bulk";

    public List<BulkFlashcardResponse> fetchFlashcards(List<String> words) {

        ExternalDictionaryRequest request = new ExternalDictionaryRequest();
        request.setWords(words);

        ResponseEntity<List<BulkFlashcardResponse>> response =
                restTemplate.exchange(
                        DICT_API_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        new ParameterizedTypeReference<>() {}
                );

        return response.getBody();
    }
}
