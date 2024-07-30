package server.rebid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.BidHistoryResponseDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.service.BidService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Bid API", description = "경매 관련 API")
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    @GetMapping
    @Operation(summary = "경매 목록 조회 🔑", description = "현재 진행중인 모든 경매 목록을 조회합니다.")
    public CommonResponse<BidResponseDTO.getBids> getBids(
    ) {
        return CommonResponse.onSuccess(bidService.getBids());
    }

    @GetMapping("/{bidId}")
    @Operation(summary = "경매 상세 조회 🔑", description = "경매 상세 정보를 조회합니다.")
    public CommonResponse<BidResponseDTO.getBidDetails> getBid(
            @AuthenticationPrincipal final CustomOAuth2User user,
            @PathVariable final Long bidId
    ) {
        if (user == null) {
            // 토큰이 존재하지 않는 경우의 처리
            return CommonResponse.onSuccess(bidService.getBidDetailsWithOutUser(bidId));
        }
        // 토큰이 존재하는 경우의 처리
        return CommonResponse.onSuccess(bidService.getBidDetails(user, bidId));
    }

    @GetMapping("/{bidId}/histories")
    @Operation(summary = "경매 입찰 내역 조회", description = "경매 입찰 내역을 조회합니다.")
    public CommonResponse<BidHistoryResponseDTO.getBidHistories> getBidHistories(
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.getBidHistories(bidId));
    }

    @GetMapping("/imminent")
    @Operation(summary = "마감 임박 경매 목록 조회 🔑", description = "마감이 임박한 경매 목록을 조회합니다.")
    public CommonResponse<BidResponseDTO.getBids> getImminentBids(
    ) {
        return CommonResponse.onSuccess(bidService.getImminentBids());
    }

    @GetMapping("/{bidId}/rejectReason")
@Operation(summary = "등록 거절 사유 조회 🔑", description = "경매 등록 거절 사유를 조회합니다.")
    public CommonResponse<BidResponseDTO.getRejectReason> getRejectReason(
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.getRejectReason(bidId));
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 별 경매 목록 조회 🔑", description = "제품 카테고리 별 경매 목록을 조회합니다.")
    public CommonResponse<BidResponseDTO.getBids> getBidsByCategory(
            @RequestParam final String name
    ) {
        return CommonResponse.onSuccess(bidService.getBidsByCategory(name));
    }

    @PostMapping("/sell")
    @Operation(summary = "경매 등록하기 🔑", description = "제품을 경매에 등록합니다.")
    public CommonResponse<BidResponseDTO.addBid> addBid(
            @AuthenticationPrincipal final CustomOAuth2User user,
            @RequestBody final BidRequestDTO.addBid request
    ) {
        return CommonResponse.onSuccess(bidService.addBid(user, request));
    }

    @PostMapping("/{bidId}/buy")
    @Operation(summary = "경매 입찰하기 🔑", description = "경매 입찰가를 등록합니다.")
    public CommonResponse<BidResponseDTO.addBidHistory> addBidHistory(
            @AuthenticationPrincipal final CustomOAuth2User user,
            @PathVariable final Long bidId,
            @RequestBody final BidRequestDTO.addBidHistory request
    ) {
        return CommonResponse.onSuccess(bidService.addBidHistory(user, bidId, request));
    }

    @PostMapping("/{bidId}/heart")
    @Operation(summary = "경매 찜하기 🔑", description = "경매를 찜합니다.")
    public CommonResponse<BidResponseDTO.addHeart> addBidHistory(
            @AuthenticationPrincipal final CustomOAuth2User user,
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.addHeart(user, bidId));
    }
}
