package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.VocabRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.VocabResponse;
import ptit.com.enghub.service.IService.VocabService;

import java.util.List;

@RestController
@RequestMapping("/api/vocabs")
@RequiredArgsConstructor
public class VocabController {

    private final VocabService vocabService;

    @PostMapping
    public ResponseEntity<ApiResponse<VocabResponse>> createVocab(@RequestBody VocabRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(vocabService.createVocab(dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VocabResponse>> getVocab(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(vocabService.getVocab(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VocabResponse>>> getAllVocabs() {
        return ResponseEntity.ok(ApiResponse.success(vocabService.getAllVocabs()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VocabResponse>> updateVocab(@PathVariable Long id, @RequestBody VocabRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(vocabService.updateVocab(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVocab(@PathVariable Long id) {
        vocabService.deleteVocab(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
