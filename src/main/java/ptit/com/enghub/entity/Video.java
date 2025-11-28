package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer duration;
}