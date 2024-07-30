package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.Bid;
import server.rebid.entity.Heart;
import server.rebid.entity.Member;
import server.rebid.repository.HeartRepository;

@Service
@RequiredArgsConstructor
public class HeartCommandService {

    private final HeartRepository heartRepository;

    public Heart addHeart(Heart heart) {
        return heartRepository.save(heart);
    }

    public void deleteHeart(Member member, Bid bid) {
        Heart heart = heartRepository.findByMemberAndBid(member, bid);
        heartRepository.delete(heart);
    }
}
