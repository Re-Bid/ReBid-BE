package server.rebid.mapper;

import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.Bid;

import java.util.List;
import java.util.stream.Collectors;

public class BidMapper {

    public static BidResponseDTO.getBid toGetBid(Bid bid) {
        return BidResponseDTO.getBid.builder()
                .bidId(bid.getId())
                .itemName(bid.getProductName())
                .imageUrl(bid.getImageUrl())
                .build();
    }

    public static BidResponseDTO.getBids toGetBids(List<Bid> bids) {
        return BidResponseDTO.getBids.builder()
                .bids(bids.stream().map(BidMapper::toGetBid).collect(Collectors.toList()))
                .build();
    }
}
