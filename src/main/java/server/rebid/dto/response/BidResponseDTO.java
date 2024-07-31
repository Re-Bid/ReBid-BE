package server.rebid.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BidResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addBid {
        private Long bidId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getBid {

        private Long bidId;
        private String itemName;
        private String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getBidDetails {

        private Long bidId;
        private String bidType;
        private String memberName;
        private String itemName;
        private String itemIntro;
        private String itemDescription;
        private List<String> imageUrls;
        private Integer startPrice;
        private Integer currentPrice;
        private LocalDateTime endDate;
        private Boolean isHeart;
        private Boolean canPurchase;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getBids {
        private List<getBid> bids;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getRejectReason {
        private String rejectReason;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addBidHistory {
        private Long bidId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addHeart {
        private Long bidId;
        private Boolean isHeart;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMemberHeart{
        private List<MemberHeartInfo> bids;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberHeartInfo{
        private Long bidId;
        private String itemName;
        private String itemIntro;
        private String imageUrl;
        private Integer startPrice;
    }

}
