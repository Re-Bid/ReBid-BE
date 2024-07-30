package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.Bid;
import server.rebid.repository.BidRepository;

@Service
@RequiredArgsConstructor
public class BidCommandService {

    private final BidRepository bidRepository;

    public Bid addBid(Bid bid) {
        return bidRepository.save(bid);
    }
}
