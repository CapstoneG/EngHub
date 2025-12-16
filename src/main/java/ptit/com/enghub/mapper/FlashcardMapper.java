package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.com.enghub.dto.request.FlashcardRequest;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.entity.DeckFlashcard;
import ptit.com.enghub.entity.Flashcard;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    default List<Long> mapDecksToIds(java.util.List<DeckFlashcard> deckFlashcards) {
        if (deckFlashcards == null) {
            return java.util.Collections.emptyList();
        }
        return deckFlashcards.stream()
                .map(df -> df.getDeck().getId())
                .collect(java.util.stream.Collectors.toList());
    }

    @Mapping(target = "deckIds", source = "deckFlashcards")
    FlashcardResponse toResponse(Flashcard flashcard);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deckFlashcards", ignore = true)
    @Mapping(target = "nextReviewAt", ignore = true)
    @Mapping(target = "repetitions", constant = "0")
    @Mapping(target = "intervalDays", constant = "0")
    @Mapping(target = "easeFactor", constant = "2.5")
    Flashcard toEntity(FlashcardRequest request);
}
