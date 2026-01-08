package ptit.com.enghub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptit.com.enghub.entity.Skill;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Page<Skill> findBySkillType(String skillType, Pageable pageable);
}