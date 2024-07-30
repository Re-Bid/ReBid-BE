package server.rebid.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.AdminBidRequest.ConfirmRealTimeBid;
import server.rebid.dto.request.AdminBidRequest.ConfirmReservationBid;
import server.rebid.dto.request.AdminBidRequest.RejectBidDTO;
import server.rebid.dto.response.AdminBidResponse.BidForAdminDTO;
import server.rebid.dto.response.AdminBidResponse.BidIdDTO;
import server.rebid.dto.response.AdminBidResponse.GetBidsByStatusDTO;
import server.rebid.service.BidService;

@RequestMapping("/admin/bids")
@RequiredArgsConstructor
@RestController
@Tag(name = "[관리자] 승인 관련 API")
public class AdminBidController {
    private final BidService bidService;

    @Operation(summary = "승인 대기(pending), 승인 완료(confirm) 제품 조회하기", description = "page는 defualt가 1, size는 defualt가 15, status는 pending(승인 대기), confirm(승인 완료) 영어로 둘중 하나만 보내주세요!")
    @GetMapping("")
    public CommonResponse<GetBidsByStatusDTO> getBidsPending(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "status") String status,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        GetBidsByStatusDTO response = bidService.getBidsByStatus(page, size, status, user.getRole());
        return CommonResponse.onSuccess(response);
    }

    @Operation(summary = "관리자 페이지 상품 조회")
    @GetMapping("/{bidId}")
    public CommonResponse<BidForAdminDTO> getBidForAdmin(
            @PathVariable("bidId") Long bidId,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        BidForAdminDTO response = bidService.getBidForAdmin(bidId, user.getRole());
        return CommonResponse.onSuccess(response);
    }

    @Operation(summary = "승인 대기 제품 반려하기")
    @PutMapping("/{bidId}/reject")
    public CommonResponse<BidIdDTO> rejectBid(
            @PathVariable(value = "bidId") Long bidId,
            @RequestBody RejectBidDTO requestDTO,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        BidIdDTO response = bidService.rejectBid(bidId, user.getRole(), requestDTO.getRejectReason());
        return CommonResponse.onSuccess(response);
    }

    @Operation(summary = "실시간 경매 - 승인 대기 제품 승인 완료하기")
    @PutMapping("/{bidId}/confirm/realTime")
    public CommonResponse<BidIdDTO> confirmRealTimeBid(
            @PathVariable(value = "bidId") Long bidId,
            @RequestBody ConfirmRealTimeBid requestDTO,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        BidIdDTO response = bidService.confirmRealTimeBid(bidId, user.getRole(), requestDTO);
        return CommonResponse.onSuccess(response);
    }

    @Operation(summary = "기간 경매 - 승인 대기 제품 승인 완료하기")
    @PutMapping("/{bidId}/confirm/reservation")
    public CommonResponse<BidIdDTO> confirmReservationBid(
            @PathVariable(value = "bidId") Long bidId,
            @RequestBody ConfirmReservationBid requestDTO,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        BidIdDTO response = bidService.confirmReservationBid(bidId, user.getRole(), requestDTO);
        return CommonResponse.onSuccess(response);
    }


}
