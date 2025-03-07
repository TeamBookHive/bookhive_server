package bookhive.bookhiveserver.domain.post.repository;

import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserOrderByCreatedAtDesc(User user);
}
