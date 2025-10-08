package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;
import ptit.com.enghub.enums.EnumRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private EnumRole name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
