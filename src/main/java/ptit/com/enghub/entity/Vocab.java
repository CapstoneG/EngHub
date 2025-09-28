package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vocab")
public class Vocab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private String phonetic;
    private String meaning;
    private String partOfSpeech;
    private String exampleSentence;
    private String imageUrl;
    private String audioUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "vocabs")
    private List<Topic> topics;
}
