package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.BidHistory;
import server.rebid.repository.BidHistoryRepository;

@Service
@RequiredArgsConstructor
public class BidHistoryCommandService {

    private final BidHistoryRepository bidHistoryRepository;

    public BidHistory addBidHistory(BidHistory bidHistory) {
        return bidHistoryRepository.save(bidHistory);
    }
}
