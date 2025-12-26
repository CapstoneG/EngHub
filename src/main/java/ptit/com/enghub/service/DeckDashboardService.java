package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.DeckDashboardResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.Deck;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.mapper.DeckMapper;
import ptit.com.enghub.repository.DeckRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckDashboardService {
    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;
    private final UserService userService;
    private final DeckService deckService;

    public DeckDashboardResponse getDashboard() {
        User user = userService.getCurrentUser();

        List<Deck> myDecks = deckRepository.findByOwnerId(user.getId());
        List<DeckSummaryResponse> myDeckDTOs = myDecks.stream()
                .map(deck -> deckService.getDeckSummary(deck.getId()))
                .toList();


        List<Deck> systemDecks = deckRepository.findAvailableSystemDecksForUser(user.getId());
        List<DeckSummaryResponse> systemDeckDTOs = systemDecks.stream()
                .map(deck -> deckService.getDeckSummary(deck.getId()))
                .toList();

        return DeckDashboardResponse.builder()
                .myDecks(myDeckDTOs)
                .systemDecks(systemDeckDTOs)
                .build();
    }
}
