package server.rebid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.entity.BidHistory;
import server.rebid.entity.QBid;
import server.rebid.entity.QBidHistory;

import java.util.List;

@Repository
public class BidHistoryRepository {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public BidHistoryRepository(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.entityManager = em;
    }

    @Transactional
    public BidHistory save(BidHistory bidHistory) {
        if (bidHistory.getId() == null) {
            entityManager.persist(bidHistory);
            return bidHistory;
        } else {
            return entityManager.merge(bidHistory);
        }
    }

    public List<BidHistory> getMemberOrders(Long memberId){
        QBidHistory bidHistory = QBidHistory.bidHistory;
        QBid bid = QBid.bid;
        return queryFactory.selectFrom(bidHistory)
                .join(bidHistory.bid).fetchJoin()
                .where(bidHistory.member.id.eq(memberId))
                .orderBy(bidHistory.createdAt.desc())
                .fetch();
    }

    public List<BidHistory> getBidHistory(Long bidId){
        QBidHistory bidHistory = QBidHistory.bidHistory;
        return queryFactory.selectFrom(bidHistory)
                .where(bidHistory.bid.id.eq(bidId))
                .orderBy(bidHistory.price.asc())
                .fetch();
    }


}
