package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.com.enghub.entity.VideoNote;

import java.util.List;
import java.util.Optional;

public interface VideoNoteRepository extends JpaRepository<VideoNote, Long> {

    List<VideoNote> findByVideoIdAndUserIdOrderByTimestampAsc(
            Long videoId, Long userId
    );

    Optional<VideoNote> findByIdAndUserId(Long id, Long userId);
}