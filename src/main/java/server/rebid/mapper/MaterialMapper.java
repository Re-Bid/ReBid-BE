package server.rebid.mapper;

import server.rebid.dto.response.MaterialResponse;
import server.rebid.dto.response.MaterialResponse.GetMaterial;
import server.rebid.dto.response.MaterialResponse.GetTotalMaterial;
import server.rebid.entity.Comment;
import server.rebid.entity.ItemImage;
import server.rebid.entity.Material;

import java.util.Collections;
import java.util.List;

public class MaterialMapper {
    public static GetTotalMaterial toGetTotalMaterial(List<Material> materials){
        return GetTotalMaterial.builder()
                .materials(materials.stream().map(m ->
                        MaterialResponse.SimpleMaterialInfo.builder()
                                .materialId(m.getId())
                                .title(m.getTitle())
                                .nickname(m.getMember().getNickname())
                                .createdAt(m.getCreatedAt().toLocalDate())
                                .build()
                                )
                        .toList()
                )
                .build();
    }

    public static GetMaterial toGetMaterial(Material material, List<Comment> comments) {
        return GetMaterial.builder()
                .material(
                        MaterialResponse.MaterialInfo.builder()
                                .materialId(material.getId())
                                .title(material.getTitle())
                                .description(material.getDescription())
                                .nickname(material.getMember().getNickname())
                                .profileImage(material.getItemImages().size() >0 ? material.getItemImages().get(0).getImageUrl() : null)
                                .createdAt(material.getCreatedAt())
                                .imageUrl(material.getItemImages().size() >0 ? material.getItemImages().stream().map(ItemImage::getImageUrl).toList(): Collections.emptyList())
                                .build()
                )
                .comments(
                        comments.stream().map(c -> MaterialResponse.CommentInfo.builder()
                                        .commentId(c.getId())
                                        .nickname(c.getMember().getNickname())
                                        .profileImage(c.getMember().getProfileImage())
                                        .content(c.getContent())
                                        .createdAt(c.getCreatedAt())
                                        .build())
                                .toList()
                )
                .build();
    }
}
