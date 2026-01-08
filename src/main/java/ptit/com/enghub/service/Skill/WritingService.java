package ptit.com.enghub.service.Skill;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ptit.com.enghub.dto.request.AiWordRequest;
import ptit.com.enghub.dto.response.AiWordResponse;

@Service
@RequiredArgsConstructor
public class WritingService {
    private final RestTemplate restTemplate;

    @Value("${ai.service.url}")
    private String aiServiceUrl;


    public AiWordResponse suggestWord(String word) {

        AiWordRequest request = new AiWordRequest(word);
        return restTemplate.postForObject(
                aiServiceUrl + "/api/suggest-words",
                request,
                AiWordResponse.class
        );
    }
}
