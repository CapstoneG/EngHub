package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.Vocab;

@Repository
public interface VocabRepository extends JpaRepository<Vocab, Long> {
}
