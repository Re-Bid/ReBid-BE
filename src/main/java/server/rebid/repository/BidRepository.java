package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long>, BidCustomRepository {
}
