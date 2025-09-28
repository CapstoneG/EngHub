package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.request.VocabRequest;
import ptit.com.enghub.dto.response.VocabResponse;

import java.util.List;

public interface VocabService {
    VocabResponse createVocab(VocabRequest request);
    VocabResponse updateVocab(Long id, VocabRequest request);
    void deleteVocab(Long id);
    VocabResponse getVocab(Long id);
    List<VocabResponse> getAllVocabs();

    VocabResponse addTopicsToVocab(Long vocabId, List<Long> topicIds);

}
