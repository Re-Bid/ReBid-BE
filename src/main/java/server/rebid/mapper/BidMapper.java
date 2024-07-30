package server.rebid.mapper;

import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.AdminBidResponse;
import server.rebid.dto.response.AdminBidResponse.BidForAdminDTO;
import server.rebid.dto.response.AdminBidResponse.BidIdDTO;
import server.rebid.dto.response.AdminBidResponse.BidsInfo;
import server.rebid.dto.response.AdminBidResponse.GetBidsByStatusDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.entity.Bid;
import server.rebid.entity.Category;
import server.rebid.entity.ItemImage;
import server.rebid.entity.enums.BidType;
import server.rebid.entity.enums.ConfirmStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BidMapper {

    public static Bid toBid(BidRequestDTO.addBid request, Category category) {
        return Bid.builder()
                .itemName(request.getItemName())
                .bidType(BidType.valueOf(request.getBidType()))
                .category(category)
                .startingPrice(request.getStartPrice())
                .itemIntro(request.getItemIntro())
                .itemDescription(request.getItemDescription())
                .itemImages(new ArrayList<>())
                .endDate(request.getEndDate())
                .build();
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
                .imageUrl(null) //TODO: 1개의 이미지만 가져오도록 수정
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

    public static BidResponseDTO.getBidDetails toGetBidDetails(Bid bid, String imageUrl) {
        return BidResponseDTO.getBidDetails.builder()
                .bidId(bid.getId())
                .bidType(String.valueOf(bid.getBidType()))
                .itemName(bid.getItemName())
                .itemIntro(bid.getItemIntro())
                .itemDescription(bid.getItemDescription())
                .imageUrl(imageUrl)
                .startPrice(bid.getStartingPrice())
                .currentPrice(null)
                .endDate(bid.getEndDate())
                .isHeart(null)
                .build();
    }

    public static GetBidsByStatusDTO toGetBidsByStatusDTO(List<Bid> bids){
        return GetBidsByStatusDTO.builder()
                .bids(bids.stream().map(bid ->
                                BidsInfo.builder()
                                        .bidId(bid.getId())
                                        .itemName(bid.getItemName())
                                        .imageUrl(bid.getItemImages().get(0).getImageUrl())
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
                .bidType(bid.getBidType().getDescription())
                .canReject(bid.getConfirmStatus().equals(ConfirmStatus.PENDING_CONFIRM))
                .canConfirm(bid.getConfirmStatus().equals(ConfirmStatus.PENDING_CONFIRM))
                .build();
    }
}
