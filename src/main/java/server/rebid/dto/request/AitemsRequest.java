package server.rebid.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AitemsRequest {
    private List<String> type;
    private String description;
    private HpConfig hpConfig;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HpConfig{
        private Boolean is_enabled;
        private String option;
    }
}
