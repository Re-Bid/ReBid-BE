package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
