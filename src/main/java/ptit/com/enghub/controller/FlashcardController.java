//package ptit.com.enghub.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ptit.com.enghub.dto.response.FlashcardResponse;
//
//@RestController
//@RequestMapping("/api/flashcards")
//@RequiredArgsConstructor
//public class FlashcardController {
//
//    private final FlashcardService flashcardService;
//
//    // 1. Thêm thẻ mới vào bộ
//    // POST /api/flashcards
//    @PostMapping
//    public ResponseEntity<FlashcardResponse> createFlashcard(@RequestBody FlashcardRequest request) {
//        return ResponseEntity.ok(flashcardService.createFlashcard(request));
//    }
//
//    // 2. Sửa nội dung thẻ (VD: sửa nghĩa tiếng Việt)
//    // PUT /api/flashcards/{id}
//    @PutMapping("/{id}")
//    public ResponseEntity<FlashcardResponse> updateFlashcard(
//            @PathVariable Long id,
//            @RequestBody FlashcardRequest request) {
//        return ResponseEntity.ok(flashcardService.updateFlashcard(id, request));
//    }
//
//    // 3. Xóa thẻ
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
//        flashcardService.deleteFlashcard(id);
//        return ResponseEntity.ok().build();
//    }
//}
