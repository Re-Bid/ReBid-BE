package server.rebid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Heart;
import server.rebid.entity.QHeart;

import java.util.List;

@Repository
public class HeartQueryRepository {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public HeartQueryRepository(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.entityManager = em;
    }


    public List<Heart> getMemberHeart(Long memberId) {
        QHeart heart = QHeart.heart;
        return queryFactory.selectFrom(heart)
                .join(heart.bid)
                .where(heart.member.id.eq(memberId))
                .fetch();
    }
}
