package server.rebid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Bid;
import server.rebid.entity.QBid;

import java.util.List;

@Repository
public class BidCustomRepositoryImpl implements BidCustomRepository{
    private final JPAQueryFactory queryFactory;

    public BidCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Bid> getMemberSales(Long memberId) {
        QBid bid = QBid.bid;
        return queryFactory.selectFrom(bid)
                .where(bid.member.id.eq(memberId))
                .orderBy(bid.createdAt.desc())
                .fetch();

    }
}
