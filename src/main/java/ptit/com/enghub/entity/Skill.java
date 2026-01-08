package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String level;

    @Column(name = "audio_url")
    private String mediaUrl;

    @Column(nullable = false)
    private String topic;

    @Column(name = "thumbnail_url")
    private String thumbnail;

    @Column(name = "skill_type")
    private String skillType;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String metadata;
}
