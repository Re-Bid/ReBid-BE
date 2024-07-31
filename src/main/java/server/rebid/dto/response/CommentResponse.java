package server.rebid.dto.response;

import lombok.Builder;
import lombok.Getter;

public class CommentResponse {

    @Getter
    @Builder
    public static class CommentId{
        private Long commentId;
    }
}
