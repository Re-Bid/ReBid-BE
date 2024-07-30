package server.rebid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.BidHistoryResponseDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.dto.response.BidResponseDTO.getMemberHeart;
import server.rebid.dto.response.ChatMemberResponse;
import server.rebid.service.BidService;
import server.rebid.service.command.BidHistoryCommandService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Bid API", description = "경매 관련 API")
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;
    private final BidHistoryCommandService bidHistoryCommandService;

    @GetMapping
    @Operation(summary = "경매 목록 조회 ", description = "현재 진행중인 모든 경매 목록을 조회합니다.")
    public CommonResponse<BidResponseDTO.getBids> getBids(
    ) {
        return CommonResponse.onSuccess(bidService.getBids());
    }

    @GetMapping("/{bidId}")
    @Operation(summary = "경매 상세 조회 🔑", description = "경매 상세 정보를 조회합니다.")
    public CommonResponse<BidResponseDTO.getBidDetails> getBid(
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.getBidDetails(bidId));
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
            @RequestBody final BidRequestDTO.addBid request
    ) {
        return CommonResponse.onSuccess(bidService.addBid(request));
    }

    @PostMapping("/{bidId}/buy")
    @Operation(summary = "경매 입찰하기 🔑", description = "경매 입찰가를 등록합니다.")
    public CommonResponse<BidResponseDTO.addBidHistory> addBidHistory(
            @PathVariable final Long bidId,
            @RequestBody final BidRequestDTO.addBidHistory request
    ) {
        return CommonResponse.onSuccess(bidService.addBidHistory(bidId, request));
    }

    @PostMapping("/{bidId}/heart")
    @Operation(summary = "경매 찜하기 🔑", description = "경매를 찜합니다.")
    public CommonResponse<BidResponseDTO.addHeart> addBidHistory(
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.addHeart(bidId));
    }

    /**
     * AI가 다음 경매 금액 추천하기
     */
    @GetMapping("/{bidId}/AiRecommend")
    @Operation(summary = "AI가 다음 경매 금액 추천하기")
    public CommonResponse<ChatMemberResponse> aiRecommendNextPrice(
            @PathVariable("bidId") Long bidId
    ){
        return bidHistoryCommandService.aiRecommendNextPrice(bidId);
    }

    @GetMapping("/heart")
    @Operation(summary = "찜한 경매 조회")
    public CommonResponse<getMemberHeart> getMemberHeart(

    ){
        getMemberHeart response = bidService.getMemberHeart(user.getMemberId());
        return CommonResponse.onSuccess(response);
    }
}
