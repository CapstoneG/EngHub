package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
}
