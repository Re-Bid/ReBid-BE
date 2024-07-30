package server.rebid.mapper;

import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.Bid;
import server.rebid.entity.Heart;
import server.rebid.entity.Member;

public class HeartMapper {

    public static Heart toHeart(Member member, Bid bid) {
        return Heart.builder()
                .member(member)
                .bid(bid)
                .build();
    }

    public static BidResponseDTO.addHeart toAddHeart(Bid bid, boolean isHeart) {
        return BidResponseDTO.addHeart.builder()
                .bidId(bid.getId())
                .isHeart(isHeart)
                .build();
    }
}
