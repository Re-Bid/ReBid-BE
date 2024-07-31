package server.rebid.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class MemberResponseDTO {

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
    public static class myPage{
        private Long memberId;
        private String nickname;
        private List<OrderInfo> orders;
        private List<SaleInfo> sales;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class OrderInfo{
        private Long bidId;
        private String itemName;
        private String imageUrl;
        private Integer bidPrice;
        private LocalDateTime bidTime;
        private String bidStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SaleInfo{
        private Long bidId;
        private String itemName;
        private String imageUrl;
        private LocalDateTime bidTime;
        private String bidStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class IsMemberAddressWrittenDTO{
        private boolean isAddressWritten;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class signup {
        private Long memberId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class login {
        private Long memberId;
        private String nickname;
        private String accessToken;
        private String refreshToken;
    }


}
