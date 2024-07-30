package server.rebid.dto.response;

import lombok.*;

import java.util.List;

public class AdminBidResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GetBidsPending{
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

}
