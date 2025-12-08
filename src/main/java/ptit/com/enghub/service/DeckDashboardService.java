package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.DeckDashboardResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.Deck;
import ptit.com.enghub.mapper.DeckMapper;
import ptit.com.enghub.repository.DeckRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckDashboardService {
    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;

    public DeckDashboardResponse getDashboard(Long userId) {
        // 1. Lấy "Kho của tôi" (My Decks)
        // Bao gồm: Deck tự tạo + Deck clone về
        List<Deck> myDecks = deckRepository.findByOwnerId(userId);
        List<DeckSummaryResponse> myDeckDTOs = deckMapper.toSummaryDTOs(myDecks);

        // 2. Lấy "Cửa hàng" (System Decks)
        // Chỉ lấy những bộ public mà user CHƯA clone về
        List<Deck> systemDecks = deckRepository.findAvailableSystemDecksForUser(userId);
        List<DeckSummaryResponse> systemDeckDTOs = deckMapper.toSummaryDTOs(systemDecks);

        return DeckDashboardResponse.builder()
                .myDecks(myDeckDTOs) // Gộp chung hiển thị ở mục "Đang học/Của tôi"
                .systemDecks(systemDeckDTOs) // Mục "Khám phá thêm"
                .build();
    }
}
