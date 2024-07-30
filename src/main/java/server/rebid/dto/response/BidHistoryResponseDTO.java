package server.rebid.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BidHistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getBidHistory {
        private Long bidHistoryId;
        private String memberName;
        private Integer price;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getBidHistories {
        private List<getBidHistory> bidHistories;
    }
}
