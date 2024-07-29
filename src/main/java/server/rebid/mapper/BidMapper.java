package server.rebid.mapper;

import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.Bid;

import java.util.List;
import java.util.stream.Collectors;

public class BidMapper {

    public static BidResponseDTO.getBid toGetBid(Bid bid) {
        return BidResponseDTO.getBid.builder()
                .bidId(bid.getId())
                .itemName(bid.getItemName())
                .imageUrl(bid.getImageUrl())
                .build();
    }

    public static BidResponseDTO.getBids toGetBids(List<Bid> bids) {
        return BidResponseDTO.getBids.builder()
                .bids(bids.stream().map(BidMapper::toGetBid).collect(Collectors.toList()))
                .build();
    }

    public static BidResponseDTO.getRejectReason toGetRejectReason(String rejectReason) {
        return BidResponseDTO.getRejectReason.builder()
                .rejectReason(rejectReason)
                .build();
    }

    public static BidResponseDTO.getBidDetails toGetBidDetails(Bid bid) {
        return BidResponseDTO.getBidDetails.builder()
                .bidId(bid.getId())
                .bidType(String.valueOf(bid.getBidType()))
                .itemName(bid.getItemName())
                .itemIntro(bid.getItemIntro())
                .itemDescription(bid.getItemDescription())
                .imageUrl(bid.getImageUrl())
                .startPrice(bid.getStartingPrice())
                .currentPrice(null)
                .endDate(bid.getEndDate())
                .isHeart(null)
                .build();
    }
}
