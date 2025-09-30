package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.TopicRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.TopicResponse;
import ptit.com.enghub.service.IService.TopicService;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<ApiResponse<TopicResponse>> createTopic(@RequestBody TopicRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(topicService.createTopic(dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(topicService.getTopic(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics() {
        return ResponseEntity.ok(ApiResponse.success(topicService.getAllTopics()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> updateTopic(@PathVariable Long id, @RequestBody TopicRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(topicService.updateTopic(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{topicId}/vocabs/{vocabId}")
    public ResponseEntity<ApiResponse<TopicResponse>> addVocabToTopic(@PathVariable Long topicId, @PathVariable Long vocabId) {
        return ResponseEntity.ok(ApiResponse.success(topicService.addVocabToTopic(topicId, vocabId)));
    }

    @DeleteMapping("/{topicId}/vocabs/{vocabId}")
    public ResponseEntity<ApiResponse<TopicResponse>> removeVocabFromTopic(@PathVariable Long topicId, @PathVariable Long vocabId) {
        return ResponseEntity.ok(ApiResponse.success(topicService.removeVocabFromTopic(topicId, vocabId)));
    }
}
