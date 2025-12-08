package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.response.DeckDashboardResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.service.DeckDashboardService;
import ptit.com.enghub.service.DeckService;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckDashboardService dashboardService;
    private final DeckService deckService;

    // 1. API Dashboard: Lấy toàn bộ dữ liệu trang chủ
    // GET /api/decks/dashboard?userId=1
    @GetMapping("/dashboard")
    public ResponseEntity<DeckDashboardResponse> getDashboard(@RequestParam Long userId) {
        // Gọi service đã viết ở bước trước để lấy MyDecks và SystemDecks
        return ResponseEntity.ok(dashboardService.getDashboard(userId));
    }

    // 2. API: Tạo bộ từ mới
    // POST /api/decks?userId=1
//    @PostMapping
//    public ResponseEntity<DeckSummaryResponse> createDeck(
//            @RequestParam Long userId,
//            @RequestBody DeckCreationRequest request) {
//        return ResponseEntity.ok(deckService.createDeck(userId, request));
//    }

    // 3. API: Clone bộ từ có sẵn về kho của mình
    // POST /api/decks/{deckId}/clone?userId=1
    @PostMapping("/{deckId}/clone")
    public ResponseEntity<Void> cloneDeck(
            @PathVariable Long deckId,
            @RequestParam Long userId) {
        deckService.cloneDeck(userId, deckId);
        return ResponseEntity.ok().build();
    }

    // 4. API: Xóa bộ từ
//    @DeleteMapping("/{deckId}")
//    public ResponseEntity<Void> deleteDeck(@PathVariable Long deckId) {
//        deckService.deleteDeck(deckId);
//        return ResponseEntity.ok().build();
//    }
}
