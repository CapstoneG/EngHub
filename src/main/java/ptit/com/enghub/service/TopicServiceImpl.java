package ptit.com.enghub.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.TopicRequest;
import ptit.com.enghub.dto.response.TopicResponse;
import ptit.com.enghub.entity.Topic;
import ptit.com.enghub.entity.Vocab;
import ptit.com.enghub.exception.ResourceNotFoundException;
import ptit.com.enghub.mapper.TopicMapper;
import ptit.com.enghub.mapper.VocabMapper;
import ptit.com.enghub.repository.TopicRepository;
import ptit.com.enghub.repository.VocabRepository;
import ptit.com.enghub.service.IService.TopicService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final VocabRepository vocabRepository;
    private final TopicMapper topicMapper;
    private final VocabMapper vocabMapper;

    @Override
    public TopicResponse createTopic(TopicRequest request) {
        Topic topic = topicMapper.toEntity(request);
        topic.setCreatedAt(LocalDateTime.now());
        topic.setUpdatedAt(LocalDateTime.now());

        if (request.getVocabIds() != null && !request.getVocabIds().isEmpty()) {
            List<Vocab> vocabs = vocabRepository.findAllById(request.getVocabIds());
            topic.setVocabs(vocabs);
        }

        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Override
    public TopicResponse updateTopic(Long id, TopicRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        topic.setName(request.getName());
        topic.setDescription(request.getDescription());
        topic.setLevel(request.getLevel());
        topic.setImageUrl(request.getImageUrl());
        topic.setUpdatedAt(LocalDateTime.now());

        if (request.getVocabIds() != null) {
            List<Vocab> vocabs = vocabRepository.findAllById(request.getVocabIds());
            topic.setVocabs(vocabs);
        }

        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Override
    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    @Override
    public TopicResponse getTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        return topicMapper.toResponse(topic);
    }

    @Override
    public List<TopicResponse> getAllTopics() {
        return topicMapper.toResponseList(topicRepository.findAll());
    }

    @Override
    public TopicResponse addVocabToTopic(Long topicId, Long vocabId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));
        Vocab vocab = vocabRepository.findById(vocabId)
                .orElseThrow(() -> new ResourceNotFoundException("Vocab not found with id: " + vocabId));

        topic.getVocabs().add(vocab);
        topicRepository.save(topic);
        return topicMapper.toResponse(topic);
    }

    @Override
    public TopicResponse removeVocabFromTopic(Long topicId, Long vocabId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));
        Vocab vocab = vocabRepository.findById(vocabId)
                .orElseThrow(() -> new ResourceNotFoundException("Vocab not found with id: " + vocabId));

        topic.getVocabs().remove(vocab);
        topicRepository.save(topic);
        return topicMapper.toResponse(topic);
    }
}