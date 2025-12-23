package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptit.com.enghub.enums.AchievementType;
import ptit.com.enghub.enums.ConditionType;

@Entity
@Table(name = "achievements")
@Getter
@Setter
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;

    private int conditionValue;

    private int rewardXp;

    private String iconUrl;
}
