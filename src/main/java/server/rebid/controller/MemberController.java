package server.rebid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.response.MemberResponse;
import server.rebid.service.MemberCommandService;

import static server.rebid.dto.request.MemberRequest.AddAddressDTO;
import static server.rebid.dto.response.MemberResponse.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberCommandService memberCommandService;

     //주소 입력
    @PostMapping("/address")
    public CommonResponse<MemberIdDTO> modifyAddress(
            @Valid @RequestBody AddAddressDTO requestDTO,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        MemberIdDTO response = memberCommandService.modifyAddress(user.getMemberId(), requestDTO.getAddress());
        return CommonResponse.onSuccess(response);
    }

    // 마이 페이지
    @GetMapping("/myPage")
    public CommonResponse<?> getMyPage(
            @AuthenticationPrincipal CustomOAuth2User user
    ){

    }


}
