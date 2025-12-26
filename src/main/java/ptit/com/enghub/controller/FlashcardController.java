package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.FlashcardRequest;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.service.FlashcardService;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    @PostMapping
    public ResponseEntity<FlashcardResponse> createFlashcard(@RequestBody FlashcardRequest request) {
        return ResponseEntity.ok(flashcardService.createFlashcard(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardResponse> updateFlashcard(
            @PathVariable Long id,
            @RequestBody FlashcardRequest request) {
        return ResponseEntity.ok(flashcardService.updateFlashcard(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FlashcardResponse>> getFlashcards(
            @RequestParam Long deckId
    ) {
        return ResponseEntity.ok(
                flashcardService.getFlashcardsByDeckId(deckId)
        );
    }
}
