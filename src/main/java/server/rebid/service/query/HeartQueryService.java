package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.Bid;
import server.rebid.entity.Member;
import server.rebid.repository.HeartRepository;

@Service
@RequiredArgsConstructor
public class HeartQueryService {

    private final HeartRepository heartRepository;

    public Boolean existsByMemberAndBid(Member member, Bid bid) {
        return heartRepository.existsByMemberAndBid(member, bid);
    }
}
