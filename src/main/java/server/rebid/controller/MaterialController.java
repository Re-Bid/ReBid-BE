package server.rebid.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.CommentRequest.AddComment;
import server.rebid.dto.request.MaterialRequest.AddMaterial;
import server.rebid.dto.response.CommentResponse.CommentId;
import server.rebid.dto.response.MaterialResponse.GetMaterial;
import server.rebid.dto.response.MaterialResponse.GetTotalMaterial;
import server.rebid.dto.response.MaterialResponse.MaterialId;
import server.rebid.service.command.CommentCommandService;
import server.rebid.service.command.MaterialCommandService;
import server.rebid.service.query.MaterialQueryService;

@RestController
@RequestMapping("/material")
@RequiredArgsConstructor
@Tag(name = "재료 도메인")
public class MaterialController {
    private final MaterialQueryService materialQueryService;
    private final CommentCommandService commentCommandService;
    private final MaterialCommandService materialCommandService;

    // 전체 재고글 조회
    @GetMapping("")
    public CommonResponse<GetTotalMaterial> getTotalMaterial(
    ){
        GetTotalMaterial response = materialQueryService.getTotalMaterial();
        return CommonResponse.onSuccess(response);
    }

    // 재고 등록
    @PostMapping("/add")
    public CommonResponse<MaterialId> addMaterial(
        @AuthenticationPrincipal CustomOAuth2User user,
        @RequestBody AddMaterial requestDTO
    ){
        MaterialId response = materialCommandService.addMaterial(user.getMemberId(), requestDTO);
        return CommonResponse.onSuccess(response);
    }

    // 특정 재료글 조회 (w/ 댓글)
    @GetMapping("/{materialId}")
    public CommonResponse<GetMaterial> getOneMaterial(
            @PathVariable Long materialId
    ){
        GetMaterial response = materialQueryService.getOneMaterial(materialId);
        return CommonResponse.onSuccess(response);
    }

    // 댓글 등록, 토큰 필요
    @PostMapping("/{materialId}/comment")
    public CommonResponse<CommentId> addComment(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long materialId,
            @RequestBody AddComment requestDTO
    ){
        CommentId response = commentCommandService.addComment(user.getMemberId(), materialId, requestDTO.getContent());
        return CommonResponse.onSuccess(response);
    }
}
