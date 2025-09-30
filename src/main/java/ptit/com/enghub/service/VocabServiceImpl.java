package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.VocabRequest;
import ptit.com.enghub.dto.response.VocabResponse;
import ptit.com.enghub.entity.Topic;
import ptit.com.enghub.entity.Vocab;
import ptit.com.enghub.mapper.TopicMapper;
import ptit.com.enghub.mapper.VocabMapper;
import ptit.com.enghub.repository.TopicRepository;
import ptit.com.enghub.repository.VocabRepository;
import ptit.com.enghub.service.IService.VocabService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VocabServiceImpl implements VocabService {

    private final VocabRepository vocabRepository;
    private final TopicRepository topicRepository;
    private final VocabMapper vocabMapper;
    private final TopicMapper topicMapper;

    @Override
    public VocabResponse createVocab(VocabRequest request) {
        Vocab vocab = vocabMapper.toEntity(request);
        vocab.setCreatedAt(LocalDateTime.now());
        vocab.setUpdatedAt(LocalDateTime.now());

        if (request.getTopicIds() != null && !request.getTopicIds().isEmpty()) {
            List<Topic> topics = topicRepository.findAllById(request.getTopicIds());
            vocab.setTopics(topics);
        }

        return vocabMapper.toResponse(vocabRepository.save(vocab));
    }

    @Override
    public VocabResponse updateVocab(Long id, VocabRequest request) {
        Vocab vocab = vocabRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocab not found"));
        vocab.setWord(request.getWord());
        vocab.setPhonetic(request.getPhonetic());
        vocab.setMeaning(request.getMeaning());
        vocab.setPartOfSpeech(request.getPartOfSpeech());
        vocab.setExampleSentence(request.getExampleSentence());
        vocab.setImageUrl(request.getImageUrl());
        vocab.setAudioUrl(request.getAudioUrl());
        vocab.setUpdatedAt(LocalDateTime.now());

        if (request.getTopicIds() != null) {
            List<Topic> topics = topicRepository.findAllById(request.getTopicIds());
            vocab.setTopics(topics);
        }

        return vocabMapper.toResponse(vocabRepository.save(vocab));
    }

    @Override
    public void deleteVocab(Long id) {
        vocabRepository.deleteById(id);
    }

    @Override
    public VocabResponse getVocab(Long id) {
        Vocab vocab = vocabRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocab not found"));
        return vocabMapper.toResponse(vocab);
    }

    @Override
    public List<VocabResponse> getAllVocabs() {
        return vocabMapper.toResponseList(vocabRepository.findAll());
    }

    @Override
    public VocabResponse addTopicsToVocab(Long vocabId, List<Long> topicIds) {
        Vocab vocab = vocabRepository.findById(vocabId)
                .orElseThrow(() -> new RuntimeException("Vocab not found"));

        List<Topic> topics = topicRepository.findAllById(topicIds);
        vocab.getTopics().addAll(topics);
        vocab.setUpdatedAt(LocalDateTime.now());

        return vocabMapper.toResponse(vocabRepository.save(vocab));
    }
}
