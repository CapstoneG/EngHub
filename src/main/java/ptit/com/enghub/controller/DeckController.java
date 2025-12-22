package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.DeckCreationRequest;
import ptit.com.enghub.dto.response.DeckDashboardResponse;
import ptit.com.enghub.dto.response.DeckStudyStatsResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.service.DeckDashboardService;
import ptit.com.enghub.service.DeckService;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckDashboardService dashboardService;
    private final DeckService deckService;

    @GetMapping("/dashboard")
    public ResponseEntity<DeckDashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }

    @PostMapping
    public ResponseEntity<DeckSummaryResponse> createDeck(
            @RequestBody DeckCreationRequest request) {
        return ResponseEntity.ok(deckService.createDeck(request));
    }

    @PostMapping("/{deckId}/clone")
    public ResponseEntity<DeckSummaryResponse> cloneDeck(
            @PathVariable Long deckId
    ) {
        DeckSummaryResponse cloneDeck = deckService.cloneDeck(deckId);
        return ResponseEntity.ok(cloneDeck);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Void> deleteDeck(@PathVariable Long deckId) {
        deckService.deleteDeck(deckId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deckId}/reset")
    public ResponseEntity<Void> resetDeckProgress(@PathVariable Long deckId) {
        deckService.resetDeckProgress(deckId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{deckId}/stats")
    public ResponseEntity<DeckStudyStatsResponse> getDeckStats(
            @PathVariable Long deckId
    ) {
        return ResponseEntity.ok(
                deckService.getDeckStats(deckId)
        );
    }
}
