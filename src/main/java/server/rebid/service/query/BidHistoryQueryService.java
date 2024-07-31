package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.entity.Member;
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

    public Boolean getCanPurchase(Member member, Bid bid) {
        BidHistory bidHistory = bidHistoryRepository.findLatest(bid);
        if(bidHistory == null){
            return false;
        }
        if(member.equals(bidHistory.getMember())){
            return true;
        }
        return false;
    }

}
