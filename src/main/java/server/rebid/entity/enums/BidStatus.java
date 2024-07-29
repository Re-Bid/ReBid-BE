package server.rebid.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BidStatus {
    COMPLETE_BID("입찰 완료"),
    REJECT_BID("유찰")
    ;

    private final String description;
}
