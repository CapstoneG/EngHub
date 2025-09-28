package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.Topic;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByName(String name);
}
