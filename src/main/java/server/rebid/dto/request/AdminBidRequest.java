package server.rebid.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AdminBidRequest {
    @Getter
    @NoArgsConstructor
    public static class RejectBidDTO{
        private String rejectReason;
    }

    @Getter
    @NoArgsConstructor
    public static class ConfirmReservationBid {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @NoArgsConstructor
    public static class ConfirmRealTimeBid{
        private LocalDateTime startDate;
    }
}
