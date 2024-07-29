package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Bid;
import server.rebid.repository.BidRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidQueryService {

    private final BidRepository bidRepository;

    public List<Bid> findAll() {
        return bidRepository.findActiveBids(LocalDateTime.now());
    }

    public List<Bid> getRealTimeBids() {
        return bidRepository.findRealTimeBids(LocalDateTime.now());
    }

    public List<Bid> getImminentBids() {
        return bidRepository.findImminentBids(LocalDateTime.now());
    }

    public List<Bid> getBidsByCategory(String categoryName) {
        return bidRepository.findBidsByCategory(LocalDateTime.now(), categoryName);
    }

    public Bid findById(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new GeneralException(GlobalErrorCode.BID_NOT_FOUND));
    }
}
