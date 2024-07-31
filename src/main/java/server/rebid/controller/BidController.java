package server.rebid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.CustomUserDetails;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.BidHistoryResponseDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.dto.response.BidResponseDTO.getBids;
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
    public CommonResponse<getBids> getBids(
    ) {
        return CommonResponse.onSuccess(bidService.getBids());
    }

    @GetMapping("/{bidId}")
    @Operation(summary = "경매 상세 조회 🔑", description = "경매 상세 정보를 조회합니다.")
    public CommonResponse<BidResponseDTO.getBidDetails> getBid(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable final Long bidId
    ) {
        if (user == null) return CommonResponse.onSuccess(bidService.getBidDetailsWithOutUser(bidId));
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
    public CommonResponse<getBids> getImminentBids(
    ) {
        return CommonResponse.onSuccess(bidService.getImminentBids());
    }

    @GetMapping("/{bidId}/rejectReason")
@Operation(summary = "등록 거절 사유 조회 🔑", description = "경매 등록 거절 사유를 조회합니다.")
    public CommonResponse<BidResponseDTO.getRejectReason> getRejectReason(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.getRejectReason(user, bidId));
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 별 경매 목록 조회", description = "제품 카테고리 별 경매 목록을 조회합니다.")
    public CommonResponse<getBids> getBidsByCategory(
            @RequestParam final String name
    ) {
        return CommonResponse.onSuccess(bidService.getBidsByCategory(name));
    }

    @PostMapping("/sell")
    @Operation(summary = "경매 등록하기 🔑", description = "제품을 경매에 등록합니다.")
    public CommonResponse<BidResponseDTO.addBid> addBid(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody final BidRequestDTO.addBid request
    ) {
        return CommonResponse.onSuccess(bidService.addBid(user, request));
    }

    @PostMapping("/{bidId}/buy")
    @Operation(summary = "경매 입찰하기 🔑", description = "경매 입찰가를 등록합니다.")
    public CommonResponse<BidResponseDTO.addBidHistory> addBidHistory(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable final Long bidId,
            @RequestBody final BidRequestDTO.addBidHistory request
    ) {
        return CommonResponse.onSuccess(bidService.addBidHistory(user, bidId, request));
    }

    @PostMapping("/{bidId}/heart")
    @Operation(summary = "경매 찜하기 🔑", description = "경매를 찜합니다.")
    public CommonResponse<BidResponseDTO.addHeart> addBidHistory(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable final Long bidId
    ) {
        return CommonResponse.onSuccess(bidService.addHeart(user, bidId));
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
            @AuthenticationPrincipal CustomUserDetails user

    ){
        getMemberHeart response = bidService.getMemberHeart(user);
        return CommonResponse.onSuccess(response);
    }


    /**
     * AI가 카테고리별 인기 제품 추천
     */
    @GetMapping("/category/{categoryId}/recommend")
    @Operation(summary = "AI가 카테고리별 인기 추천")
    public CommonResponse<getBids> getCategoryRecommend(
            @PathVariable("categoryId") Long categoryId
    ){
        getBids response = bidService.getCategoryRecommend(categoryId);
        return CommonResponse.onSuccess(response);
    }

    /**
     * AI가 개인별 상품 추천
     */
    @GetMapping("/personalRecommend")
    @Operation(summary = "AI가 개인별 상품 추천")
    public CommonResponse<getBids> getPersonalCommend(){
        // TODO : 여기 유저 정보 추가 !!!!!
        Long memberId = 1L;
        getBids response = bidService.getPersonalRecommend(memberId);
        return CommonResponse.onSuccess(response);
    }

//    @PostMapping("/requestLearning")
//    public CommonResponse<String> requestBidLearning(){
//        bidService.requestBidLearning();
//        return CommonResponse.onSuccess("학습 요청 성공");
//    }
}
