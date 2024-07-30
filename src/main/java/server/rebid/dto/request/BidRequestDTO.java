package server.rebid.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BidRequestDTO {

    @Getter
    public static class addBid {

        private String itemName;
        private String bidType;
        private String category;
        private Integer startPrice;
        private String itemIntro;
        private String itemDescription;
        private List<String> imageUrls;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    public static class addBidHistory {

        private Integer price;
    }
}
