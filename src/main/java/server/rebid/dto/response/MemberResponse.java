package server.rebid.dto.response;

import lombok.*;

public class MemberResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberIdDTO{
        private Long memberId;
    }


}
