package server.rebid.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfirmStatus {
    PENDING_CONFIRM("승인 대기"),
    REJECT_CONFIRM("승인 거부"),
    COMPLETE_CONFIRM("승인 완료");

    private final String description;
}
