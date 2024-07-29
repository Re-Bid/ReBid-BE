package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.rebid.entity.Bid;

import java.time.LocalDateTime;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b WHERE b.startDate <= :currentTime AND (b.endDate IS NULL OR b.endDate >= :currentTime)")
    List<Bid> findActiveBids(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Bid b WHERE b.startDate <= :currentTime AND (b.endDate IS NULL OR b.endDate >= :currentTime) AND b.bidType = 'REAL_TIME'")
    List<Bid> findRealTimeBids(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Bid b WHERE b.startDate <= :currentTime AND (MONTH(b.endDate) = MONTH(:currentTime) AND DAY(b.endDate) = DAY(:currentTime)) ")
    List<Bid> findImminentBids(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Bid b WHERE b.startDate <= :currentTime AND (b.endDate IS NULL OR b.endDate >= :currentTime) AND b.category.name = :categoryName")
    List<Bid> findBidsByCategory(LocalDateTime currentTime, String categoryName);
}
