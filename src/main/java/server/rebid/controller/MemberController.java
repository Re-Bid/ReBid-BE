package server.rebid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.MemberRequestDTO;
import server.rebid.dto.response.MemberResponseDTO;
import server.rebid.service.MemberService;
import server.rebid.service.command.MemberCommandService;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "사용자 정보 확인")
public class MemberController {

    private final MemberService memberService;
    private final MemberCommandService memberCommandService;

//    @PostMapping("/signup")
//    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
//    public CommonResponse<MemberResponseDTO.signup> signup(
//            @RequestBody MemberRequestDTO.signup request
//    ){
//        return CommonResponse.onSuccess(memberService.signup(request));
//    }

    // 주소 입력했는지 확인
//    @Operation(summary = "사용자가 주소 입력했는지 확인")
//    @GetMapping("/address")
//    public CommonResponse<IsMemberAddressWrittenDTO> isMemberAddressWritten(
//            @AuthenticationPrincipal CustomOAuth2User user
//    ){
//        IsMemberAddressWrittenDTO response = memberQueryService.isMemberAddressWritten(user.getMemberId());
//        return CommonResponse.onSuccess(response);
//    }
//
     //주소 입력
    @PostMapping("/address")
    @Operation(summary = "주소 변경")
    public CommonResponse<MemberResponseDTO.MemberIdDTO> modifyAddress(
            @Valid @RequestBody MemberRequestDTO.AddAddressDTO requestDTO,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        MemberResponseDTO.MemberIdDTO response = memberCommandService.modifyAddress(user.getMemberId(), requestDTO.getAddress());
        return CommonResponse.onSuccess(response);
    }

    // 마이 페이지
    @GetMapping("/myPage")
    @Operation(summary = "마이페이지")
    public CommonResponse<MemberResponseDTO.myPage> getMyPage(
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        return CommonResponse.onSuccess(memberService.getMyPage(user));
    }


}
