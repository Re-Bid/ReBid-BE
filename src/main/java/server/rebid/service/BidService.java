package server.rebid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.Bid;
import server.rebid.mapper.BidMapper;
import server.rebid.service.query.BidQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BidService {

    private final BidQueryService bidQueryService;

    public BidResponseDTO.getBids getBids() {
        List<Bid> bids = bidQueryService.findAll();
        return BidMapper.toGetBids(bids);
    }
}
