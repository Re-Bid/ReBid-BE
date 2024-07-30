package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.rebid.entity.Bid;
import server.rebid.entity.Heart;
import server.rebid.entity.Member;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByMemberAndBid(Member member, Bid bid);

    Heart findByMemberAndBid(Member member, Bid bid);
}
