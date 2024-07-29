package server.rebid.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.response.AdminBidResponse;

@RequestMapping("/admin/bids")
@RequiredArgsConstructor
@RestController
@Tag(name = "[관리자] 승인 관련 API")
public class AdminBidController {

    @Operation(summary = "승인 대기(pending), 승인 완료(confirm) 제품 조회하기", description = "page는 defualt가 1, size는 defualt가 15, status는 pending(승인 대기), confirm(승인 완료) 영어로 둘중 하나만 보내주세요!")
    @GetMapping("")
    public CommonResponse<AdminBidResponse.GetBidsPending> getBidsPending(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "status") String status,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        return null;
    }


    @Operation(summary = "승인 대기 제품 반려하기")
    @PutMapping("/{bidId}/reject")
    public CommonResponse<?> rejectBid(
            @PathVariable(value = "bidId") Long bidId,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        return null;
    }

    @Operation(summary = "승인 대기 제품 승인 완료하기")
    @PutMapping("/{bidId}/confirm")
    public CommonResponse<?> confirmBid(
            @PathVariable(value = "bidId") Long bidId,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        return null;
    }
}
