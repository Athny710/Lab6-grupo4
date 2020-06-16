package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostComment;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByPost(Post post);
}
