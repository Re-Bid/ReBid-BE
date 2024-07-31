package server.rebid.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.CommentRequest;
import server.rebid.dto.request.CommentRequest.AddComment;
import server.rebid.dto.request.MaterialRequest;
import server.rebid.dto.request.MaterialRequest.AddMaterial;
import server.rebid.dto.response.CommentResponse;
import server.rebid.dto.response.CommentResponse.CommentId;
import server.rebid.dto.response.MaterialResponse;
import server.rebid.dto.response.MaterialResponse.GetMaterial;
import server.rebid.dto.response.MaterialResponse.GetTotalMaterial;
import server.rebid.dto.response.MaterialResponse.MaterialId;

@RestController
@RequestMapping("/material")
@RequiredArgsConstructor
@Tag(name = "재료 도메인")
public class MaterialController {

    // 전체 재고글 조회
    @GetMapping("")
    public CommonResponse<GetTotalMaterial> getTotalMaterial(
    ){
        return null;
    }

    // 재료 등록, 토큰 필요
    @PostMapping("")
    public CommonResponse<MaterialId> addMaterial(
        @RequestBody AddMaterial requestDTO
    ){
        return null;
    }

    // 특정 재료글 조회 (w/ 댓글)
    @GetMapping("/{materialId}")
    public CommonResponse<GetMaterial> getMaterial(
            @PathVariable Long materialId
    ){
        return  null;
    }

    // 댓글 등록, 토큰 필요
    @PostMapping("/{materialId}/comment")
    public CommonResponse<CommentId> addComment(
            @PathVariable Long materialId,
            @RequestBody AddComment requestDTO
    ){
        return null;
    }
}
