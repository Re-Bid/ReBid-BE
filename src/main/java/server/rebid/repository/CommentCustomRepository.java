package server.rebid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Comment;
import server.rebid.entity.QComment;

import java.util.List;

@Repository
public class CommentCustomRepository {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public CommentCustomRepository(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.entityManager = em;
    }


    public List<Comment> getComments(Long materialId) {
        QComment comment = QComment.comment;
        return queryFactory.selectFrom(comment)
                .join(comment.member).fetchJoin()
                .orderBy(comment.createdAt.asc())
                .fetch();
    }
}
