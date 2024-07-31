package server.rebid.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MaterialRequest {

    @Getter
    @NoArgsConstructor
    public static class AddMaterial{
        private List<String> imageUrl;
        private String title;
        private String description;
    }
}
