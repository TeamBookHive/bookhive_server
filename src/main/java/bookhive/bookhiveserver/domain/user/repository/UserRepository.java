package bookhive.bookhiveserver.domain.user.repository;

import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDeviceId(String deviceId);
    Optional<User> findByToken(String token);
}
