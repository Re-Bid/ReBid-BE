package server.rebid.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequest {
    private List<ChatMessage> messages;
    private Double temperature;
    private Integer topK;
    private Double topP;
    private Double repeatPenalty;
    private String stopBefore;
    private Integer maxTokens;
    private Boolean includeAiFilters;
    private Integer seed;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatMessage{
        private ChatType role;
        private String content;
    }
}
