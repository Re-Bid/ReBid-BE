package server.rebid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.response.MemberResponse.IsMemberAddressWrittenDTO;
import server.rebid.service.MemberCommandService;
import server.rebid.service.MemberQueryService;

import static server.rebid.dto.request.MemberRequest.AddAddressDTO;
import static server.rebid.dto.response.MemberResponse.MemberIdDTO;
import static server.rebid.dto.response.MemberResponse.MyPageDTO;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "사용자 정보 확인")
public class MemberController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    // 주소 입력했는지 확인
    @Operation(summary = "사용자가 주소 입력했는지 확인")
    @GetMapping("/address")
    public CommonResponse<IsMemberAddressWrittenDTO> isMemberAddressWritten(
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        IsMemberAddressWrittenDTO response = memberQueryService.isMemberAddressWritten(user.getMemberId());
        return CommonResponse.onSuccess(response);
    }

     //주소 입력
    @PostMapping("/address")
    @Operation(summary = "주소 변경")
    public CommonResponse<MemberIdDTO> modifyAddress(
            @Valid @RequestBody AddAddressDTO requestDTO,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        MemberIdDTO response = memberCommandService.modifyAddress(user.getMemberId(), requestDTO.getAddress());
        return CommonResponse.onSuccess(response);
    }

    // 마이 페이지
    @GetMapping("/myPage")
    @Operation(summary = "마이페이지")
    public CommonResponse<MyPageDTO> getMyPage(
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        memberQueryService.getMyPage(user.getMemberId());
        return null;
    }


}
