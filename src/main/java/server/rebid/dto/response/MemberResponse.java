package server.rebid.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class MemberResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberIdDTO{
        private Long memberId;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyPageDTO{
        private Long memberId;
        private String nickname;
        private List<OrderInfo> orders;
        private List<OrderInfo> sales;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class OrderInfo{
        private Long bidId;
        private String itemName;
        private String imageUrl;
        private LocalDateTime bidTime;
        private Integer bidPrice;
        private String bidStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class IsMemberAddressWrittenDTO{
        private boolean isAddressWritten;
    }


}
