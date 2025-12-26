package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.Deck;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = { FlashcardMapper.class })
public interface DeckMapper {

    @Mapping(target = "totalCards", expression = "java(deck.getDeckFlashcards() != null ? deck.getDeckFlashcards().size() : 0)")
    DeckSummaryResponse toSummaryDTO(Deck deck);

    List<DeckSummaryResponse> toSummaryDTOs(List<Deck> decks);
}
