package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.OAuth2Account;

@Repository
public interface OAuth2AccountRepository extends JpaRepository<OAuth2Account, Long> {
}