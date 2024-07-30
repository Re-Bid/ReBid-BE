package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Bid;
import server.rebid.entity.Heart;
import server.rebid.entity.Member;

import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByMemberAndBid(Member member, Bid bid);

    Heart findByMemberAndBid(Member member, Bid bid);

}
