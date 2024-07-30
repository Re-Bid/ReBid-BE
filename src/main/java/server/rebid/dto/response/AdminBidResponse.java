package server.rebid.dto.response;

import lombok.*;

import java.util.List;

public class AdminBidResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GetBidsByStatusDTO {
        private List<BidsInfo> bids;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BidsInfo{
        private Long bidId;
        private String itemName;
        private String imageUrl;
        private Integer startPrice;
        private String completeStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BidIdDTO{
        private Long bidId;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BidForAdminDTO{
        private Long bidId;
        private String itemName;
        private List<String> imageUrl;
        private String itemIntro;
        private String itemDescription;
        private Integer startPrice;
        private String bidType;
        private Boolean canReject;
        private Boolean canConfirm;
    }

}
