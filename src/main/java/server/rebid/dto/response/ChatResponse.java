package server.rebid.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatResponse {
    private Status status;
    private Result result;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Status {
        private String code;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Result {
        private Message message;
        private String stopReason;
        private Integer inputLength;
        private Integer outputLength;
        private Integer seed;
        private List<AiFilter> aiFilter;


        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        public static class Message {
            private String role;
            private String content;
        }

        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        public static class AiFilter {
            private String groupName;
            private String name;
            private String score;

            // Getters and Setters
        }
    }

}
