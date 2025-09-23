package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;
    private String role;
    private String email;
    private String avatar;
    private String level;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int streak;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int xp;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable = false, updatable = false)
    private LocalDateTime lastLogin;

}

