package server.rebid.mapper;

import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.*;
import server.rebid.entity.enums.BidType;
import server.rebid.entity.enums.ConfirmStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BidMapper {

    public static Bid toBid(Member member, BidRequestDTO.addBid request, Category category) {
        return Bid.builder()
                .itemName(request.getItemName())
                .bidType(BidType.valueOf(request.getBidType()))
                .bidCode(generateBidCode())
                .category(category)
                .member(member)
                .startingPrice(request.getStartPrice())
                .itemIntro(request.getItemIntro())
                .itemDescription(request.getItemDescription())
                .itemImages(new ArrayList<>())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .confirmStatus(ConfirmStatus.PENDING_CONFIRM)
                .build();
    }

    private static long generateBidCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);  // 100000 ~ 999999 범위의 난수 생성
    }

    public static List<ItemImage> toItemImages(BidRequestDTO.addBid request) {
        return request.getImageUrls().stream().map(imageUrl -> ItemImage.builder().imageUrl(imageUrl).build()).collect(Collectors.toList());
    }

    public static BidResponseDTO.addBid toAddBid(Bid bid) {
        return BidResponseDTO.addBid.builder()
                .bidId(bid.getId())
                .build();
    }
    public static BidResponseDTO.getBid toGetBid(Bid bid) {
        return BidResponseDTO.getBid.builder()
                .bidId(bid.getId())
                .itemName(bid.getItemName())
                .imageUrl(bid.getItemImages().get(0).getImageUrl()) // 첫 번째 이미지만 가져옴
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

    public static BidResponseDTO.getBidDetails toGetBidDetails(Bid bid, List<String> imageUrls, boolean isHeart) {
        Integer currentPrice = bid.getStartingPrice();
        if (!bid.getBidHistories().isEmpty()) {
            currentPrice = bid.getBidHistories().stream()
                    .max(Comparator.comparing(BidHistory::getPrice))
                    .map(BidHistory::getPrice)
                    .orElse(currentPrice);
        }

        return BidResponseDTO.getBidDetails.builder()
                .bidId(bid.getId())
                .bidType(String.valueOf(bid.getBidType()))
                .itemName(bid.getItemName())
                .itemIntro(bid.getItemIntro())
                .itemDescription(bid.getItemDescription())
                .imageUrls(imageUrls)
                .startPrice(bid.getStartingPrice())
                .currentPrice(currentPrice)
                .endDate(bid.getEndDate())
                .isHeart(isHeart)
                .build();
    }

    public static BidHistory toBidHistory(Member member, Bid bid, BidRequestDTO.addBidHistory request) {
        return BidHistory.builder()
                .member(member)
                .bid(bid)
                .price(request.getPrice())
                .build();
    }

    public static BidResponseDTO.addBidHistory toAddBidHistory(BidHistory savedBidHistory) {
        return BidResponseDTO.addBidHistory.builder()
                .bidId(savedBidHistory.getBid().getId())
                .build();
    }

}
