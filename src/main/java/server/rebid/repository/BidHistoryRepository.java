package server.rebid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.rebid.entity.BidHistory;
import server.rebid.entity.QBid;
import server.rebid.entity.QBidHistory;

import java.util.List;

@Repository
public class BidHistoryRepository {
    private final JPAQueryFactory queryFactory;

    public BidHistoryRepository(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<BidHistory> getMemberOrders(Long memberId){
        QBidHistory bidHistory = QBidHistory.bidHistory;
        QBid bid = QBid.bid;
        return queryFactory.selectFrom(bidHistory)
                .join(bid).fetchJoin()
                .where(bidHistory.member.id.eq(memberId))
                .orderBy(bidHistory.createdAt.desc())
                .fetch();
    }


}
