package server.rebid.mapper;

import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.AdminBidResponse;
import server.rebid.dto.response.AdminBidResponse.BidForAdminDTO;
import server.rebid.dto.response.AdminBidResponse.BidIdDTO;
import server.rebid.dto.response.AdminBidResponse.BidsInfo;
import server.rebid.dto.response.AdminBidResponse.GetBidsByStatusDTO;
import server.rebid.dto.response.BidHistoryResponseDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.*;
import server.rebid.entity.enums.BidType;
import server.rebid.entity.enums.ConfirmStatus;

import java.util.*;
import java.util.stream.Collectors;

public class BidMapper {

    public static Bid toBid(Member member, BidRequestDTO.addBid request, Category category) {
        return Bid.builder()
                .itemName(request.getItemName())
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

    public static List<ItemImage> toItemImages(List<String> imageUrl){
        return imageUrl.stream().map(imageurl -> ItemImage.builder().imageUrl(imageurl).build()).collect(Collectors.toList());
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
                .imageUrl(bid.getItemImages().size()>0 ? bid.getItemImages().get(0).getImageUrl() : null) // 첫 번째 이미지만 가져옴
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

    public static BidResponseDTO.getBidDetails toGetBidDetails(Bid bid, List<String> imageUrls, boolean isHeart, boolean canPurchase) {
        Integer currentPrice = bid.getStartingPrice();
        if (!bid.getBidHistories().isEmpty()) {
            currentPrice = bid.getBidHistories().stream()
                    .max(Comparator.comparing(BidHistory::getPrice))
                    .map(BidHistory::getPrice)
                    .orElse(currentPrice);
        }

        return BidResponseDTO.getBidDetails.builder()
                .bidId(bid.getId())
                .memberName(bid.getMember().getNickname())
                .itemName(bid.getItemName())
                .itemIntro(bid.getItemIntro())
                .itemDescription(bid.getItemDescription())
                .imageUrls(imageUrls)
                .startPrice(bid.getStartingPrice())
                .currentPrice(currentPrice)
                .endDate(bid.getEndDate())
                .isHeart(isHeart)
                .canPurchase(canPurchase)
                .build();
    }

    public static GetBidsByStatusDTO toGetBidsByStatusDTO(List<Bid> bids){
        return GetBidsByStatusDTO.builder()
                .bids(bids.stream().map(bid ->
                                BidsInfo.builder()
                                        .bidId(bid.getId())
                                        .itemName(bid.getItemName())
                                        .imageUrl(bid.getItemImages().size() > 0 ? bid.getItemImages().get(0).getImageUrl() : null)
                                        .startPrice(bid.getStartingPrice())
                                        .completeStatus(bid.getConfirmStatus().getDescription())
                                        .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static BidIdDTO toBidIdDTO(Long modifyBidId){
        return BidIdDTO.builder()
                .bidId(modifyBidId)
                .build();
    }

    public static BidForAdminDTO toBidForAdminDTO(Bid bid){
        return BidForAdminDTO.builder()
                .bidId(bid.getId())
                .itemName(bid.getItemName())
                .imageUrl(
                        bid.getItemImages().stream().map(ItemImage::getImageUrl).toList()
                )
                .itemIntro(bid.getItemIntro())
                .itemDescription(bid.getItemDescription())
                .startPrice(bid.getStartingPrice())
                .canReject(bid.getConfirmStatus().equals(ConfirmStatus.PENDING_CONFIRM))
                .canConfirm(bid.getConfirmStatus().equals(ConfirmStatus.PENDING_CONFIRM))
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

    public static BidHistoryResponseDTO.getBidHistory toGetBidHistory(BidHistory bidHistory) {
        return BidHistoryResponseDTO.getBidHistory.builder()
                .bidHistoryId(bidHistory.getId())
                .price(bidHistory.getPrice())
                .memberName(bidHistory.getMember().getNickname())
                .createdAt(bidHistory.getCreatedAt())
                .build();
    }

    public static BidHistoryResponseDTO.getBidHistories toGetBidHistories(List<BidHistory> bidHistories) {
        return BidHistoryResponseDTO.getBidHistories.builder()
                .bidHistories(bidHistories.stream().map(BidMapper::toGetBidHistory).collect(Collectors.toList()))
                .build();
    }

    public static BidResponseDTO.getMemberHeart toGetMemberHeart(List<Heart> hearts){
        return BidResponseDTO.getMemberHeart.builder()
                .bids(hearts.stream().map(
                        h -> BidResponseDTO.MemberHeartInfo.builder()
                                .bidId(h.getBid().getId())
                                .itemName(h.getBid().getItemName())
                                .itemIntro(h.getBid().getItemIntro())
                                .imageUrl(h.getBid().getItemImages().size() >0 ? h.getBid().getItemImages().get(0).getImageUrl() : null)
                                .startPrice(h.getBid().getStartingPrice())
                                .build()

                ).toList())
                .build();
    }

}
