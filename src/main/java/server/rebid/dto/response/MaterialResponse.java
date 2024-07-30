package server.rebid.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MaterialResponse {

    @Getter
    @Builder
    public static class GetTotalMaterial{
        private Long materialId;
        private String title;
        private String nickname;
        private LocalDate createdAt;
    }

    @Getter
    @Builder
    public static class MaterialId{
        private Long materialId;
    }

    @Getter
    @Builder
    public static class GetMaterial{
        private MaterialInfo material;
        private List<CommentInfo> comments;
    }

    @Getter
    @Builder
    public static class MaterialInfo{
        private Long materialId;
        private String title;
        private String description;
        private String nickname;
        private String profileImage;
        private Integer commentNum;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class CommentInfo{
        private Long commentId;
        private String content;
        private String nickname;
        private String profileImage;
        private LocalDateTime createdAt;
    }
}
