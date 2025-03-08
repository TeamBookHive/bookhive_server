package bookhive.bookhiveserver.domain.post.repository;

import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    @Query("select p from Post p where p.user = :user and p.content like %:keyword%")
    List<Post> findByUserAndKeyword(User user, String keyword);
}
