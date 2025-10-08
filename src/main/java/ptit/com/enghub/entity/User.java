package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;
import ptit.com.enghub.enums.Role;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String username;
    private String avatar;
    private String level;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int streak;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int xp;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable = false, updatable = true)
    private LocalDateTime lastLogin;

}

