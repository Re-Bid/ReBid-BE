package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

}
