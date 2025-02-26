package bookhive.bookhiveserver.domain.tag.repository;

import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserOrderByValue(User user) ;
    List<Tag> findAllByUser(User user);
    Tag findByValue(String value);
}
