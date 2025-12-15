package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.entity.Flashcard;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    default java.util.List<Long> mapDecksToIds(java.util.List<ptit.com.enghub.entity.DeckFlashcard> deckFlashcards) {
        if (deckFlashcards == null) {
            return java.util.Collections.emptyList();
        }
        return deckFlashcards.stream()
                .map(df -> df.getDeck().getId())
                .collect(java.util.stream.Collectors.toList());
    }

    @Mapping(target = "deckIds", source = "deckFlashcards")
    FlashcardResponse toResponse(Flashcard flashcard);

    // Khi tạo mới từ request thì bỏ qua ID và các thông số thuật toán
    // @Mapping(target = "id", ignore = true)
    // @Mapping(target = "deck", ignore = true) // Sẽ set tay trong Service
    // @Mapping(target = "repetitions", constant = "0")
    // @Mapping(target = "intervalDays", constant = "0")
    // @Mapping(target = "easeFactor", constant = "2.5")
    // Flashcard toEntity(FlashcardRequest request);
}
