package server.rebid.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BidType {
    REAL_TIME("실시간 경매"),
    RESERVATION("기간 경매");

    private final String description;
}
