package server.rebid.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BidStatus {
    WAITING_BID("입찰 대기"),
    ONGOING_BID("입찰 중"),
    COMPLETE_BID("입찰 완료"),
    ;

    private final String description;
}
