package bookhive.bookhiveserver.domain.post.repository;

import bookhive.bookhiveserver.domain.post.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Modifying
    @Query("delete from PostTag pt where pt.post.id = :postId")
    void deleteByPostId(@Param("postId")Long postId);

    @Modifying
    @Query(value = "INSERT INTO post_tag (post_id, tag_id) VALUES (:postId, :tagId)", nativeQuery = true)
    void insertPostTag(@Param("postId") Long postId, @Param("tagId") Long tagId);
}
