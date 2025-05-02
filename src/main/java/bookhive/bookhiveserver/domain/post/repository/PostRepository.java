package bookhive.bookhiveserver.domain.post.repository;

import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserOrderByCreatedAtDesc(User user);
    List<Post> findByUserAndBookOrderByCreatedAtDesc(User user, Book book);

    @Query("select distinct p from Post p " +
            "left join p.postTags pt " +
            "left join pt.tag t " +
            "where p.user = :user " +
            "and (p.content like %:keyword% or t.value like %:keyword%)")
    List<Post> findByUserAndKeyword(User user, String keyword);

    @Query("select p.content from Post p " +
            "join p.postTags pt " +
            "join pt.tag t " +
            "where p.user = :user and t.value = :tagValue " +
            "order by p.createdAt desc")
    List<String> findTop5ByUserAndTagValueOrderByCreatedAtDesc(User user, String tagValue, Pageable pageable);
}
