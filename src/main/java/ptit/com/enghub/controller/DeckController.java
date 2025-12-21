package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.DeckCreationRequest;
import ptit.com.enghub.dto.response.DeckDashboardResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.service.DeckDashboardService;
import ptit.com.enghub.service.DeckService;
import ptit.com.enghub.service.UserService;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckDashboardService dashboardService;
    private final DeckService deckService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<DeckDashboardResponse> getDashboard() {
        User user = userService.getUser();
        return ResponseEntity.ok(dashboardService.getDashboard(user.getId()));
    }

    @PostMapping
    public ResponseEntity<DeckSummaryResponse> createDeck(
            @RequestBody DeckCreationRequest request) {
        User user = userService.getUser();
        return ResponseEntity.ok(deckService.createDeck(user.getId(), request));
    }

    @PostMapping("/{deckId}/clone")
    public ResponseEntity<DeckSummaryResponse> cloneDeck(
            @PathVariable Long deckId
    ) {
        User user = userService.getUser();
        DeckSummaryResponse cloneDeck = deckService.cloneDeck(user.getId(), deckId);
        return ResponseEntity.ok(cloneDeck);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Void> deleteDeck(@PathVariable Long deckId) {
        deckService.deleteDeck(deckId);
        return ResponseEntity.ok().build();
    }
}
