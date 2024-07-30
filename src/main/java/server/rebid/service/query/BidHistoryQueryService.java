package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.BidHistory;
import server.rebid.repository.BidHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidHistoryQueryService {

    private final BidHistoryRepository bidHistoryRepository;

    public List<BidHistory> getMemberOrders(Long memberId) {
        return bidHistoryRepository.getMemberOrders(memberId);
    }

}
