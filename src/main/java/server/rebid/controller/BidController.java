package server.rebid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.service.BidService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    @GetMapping
    public CommonResponse<BidResponseDTO.getBids> getBids(
            @AuthenticationPrincipal final CustomOAuth2User user
    ) {
        return CommonResponse.onSuccess(bidService.getBids());
    }


}
