package server.rebid.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {
    @Getter
    @NoArgsConstructor
    public static class AddComment{
        private String content;
    }
}
