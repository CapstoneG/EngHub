package ptit.com.enghub.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ptit.com.enghub.entity.Deck;
import ptit.com.enghub.entity.DeckFlashcard;
import ptit.com.enghub.entity.Flashcard;
import ptit.com.enghub.repository.DeckRepository;
import ptit.com.enghub.repository.FlashcardRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (deckRepository.count() == 0) {
            log.info("Starting Data Seeding...");
            try {
                InputStream inputStream = TypeReference.class.getResourceAsStream("/data/seed-data.json");
                if (inputStream == null) {
                    log.warn("seed-data.json not found in /data/");
                    return;
                }
                List<DeckSeedDto> seedData = objectMapper.readValue(inputStream,
                        new TypeReference<List<DeckSeedDto>>() {
                        });

                for (DeckSeedDto deckDto : seedData) {
                    seedDeck(deckDto);
                }
                log.info("Data Seeding Completed.");

            } catch (IOException e) {
                log.error("Failed to seed data: " + e.getMessage());
            }
        } else {
            log.info("Database already contains data. Skipping seeding.");
        }
    }

    private void seedDeck(DeckSeedDto deckDto) {
        // 1. Create Deck
        Deck deck = Deck.builder()
                .name(deckDto.getName())
                .description(deckDto.getDescription())
                .ownerId(1L) // Default admin/user ID
                .creatorId(1L)
                .build();

        // Save deck to get ID
        deck = deckRepository.save(deck);

        List<Flashcard> flashcards = new ArrayList<>();
        if (deckDto.getFlashcards() != null) {
            for (FlashcardSeedDto fDto : deckDto.getFlashcards()) {
                Flashcard flashcard = Flashcard.builder()
                        .term(fDto.getTerm())
                        .phonetic(fDto.getPhonetic())
                        .definition(fDto.getDefinition())
                        .partOfSpeech(fDto.getPartOfSpeech())
                        .exampleSentence(fDto.getExampleSentence())
                        .build();

                // Create connection
                DeckFlashcard df = new DeckFlashcard();
                df.setDeck(deck);
                df.setFlashcard(flashcard);
                List<DeckFlashcard> dfs = new ArrayList<>();
                dfs.add(df);
                flashcard.setDeckFlashcards(dfs);

                flashcards.add(flashcard);
            }
            for (Flashcard f : flashcards) {
                // Ensure DF has the back-reference if needed
                f.getDeckFlashcards().get(0).setFlashcard(f);
                // ID is auto-generated, no need to set.
            }

            flashcardRepository.saveAll(flashcards);
        }
    }

    // Inner DTOs
    @lombok.Data
    static class DeckSeedDto {
        private String name;
        private String description;
        private List<FlashcardSeedDto> flashcards;
    }

    @lombok.Data
    static class FlashcardSeedDto {
        private String term;
        private String phonetic;
        private String definition;
        private String partOfSpeech;
        private String exampleSentence;
    }
}
