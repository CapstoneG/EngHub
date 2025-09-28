package ptit.com.enghub.service.IService;


import ptit.com.enghub.dto.request.TopicRequest;
import ptit.com.enghub.dto.response.TopicResponse;
import ptit.com.enghub.repository.TopicRepository;

import java.util.List;

public interface TopicService {
    TopicResponse createTopic(TopicRequest request);
    TopicResponse updateTopic(Long id, TopicRequest request);
    void deleteTopic(Long id);
    TopicResponse getTopic(Long id);
    List<TopicResponse> getAllTopics();

    TopicResponse addVocabToTopic(Long topicId, Long vocabId);
    TopicResponse removeVocabFromTopic(Long topicId, Long vocabId);
}

