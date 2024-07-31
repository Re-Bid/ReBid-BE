package server.rebid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import server.rebid.auth.CustomUserDetails;
import server.rebid.common.CommonResponse;
import server.rebid.dto.request.MemberRequestDTO;
import server.rebid.dto.response.MemberResponseDTO;
import server.rebid.dto.response.MemberResponseDTO.IsMemberAddressWrittenDTO;

import server.rebid.service.MemberService;
import server.rebid.service.query.MemberQueryService;
import server.rebid.service.command.MemberCommandService;

import static server.rebid.dto.request.MemberRequestDTO.AddAddressDTO;
import static server.rebid.dto.response.MemberResponseDTO.MemberIdDTO;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "사용자 정보 확인")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    public CommonResponse<MemberResponseDTO.signup> signup(
            @RequestBody MemberRequestDTO.signup request
    ){
        return CommonResponse.onSuccess(memberService.signup(request));
    }

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
//     //주소 입력
//    @PostMapping("/address")
//    @Operation(summary = "주소 변경")
//    public CommonResponse<MemberIdDTO> modifyAddress(
//            @Valid @RequestBody AddAddressDTO requestDTO,
//            @AuthenticationPrincipal CustomOAuth2User user
//    ){
//        MemberIdDTO response = memberCommandService.modifyAddress(user.getMemberId(), requestDTO.getAddress());
//        return CommonResponse.onSuccess(response);
//    }

    // 마이 페이지
    @GetMapping("/myPage")
    @Operation(summary = "마이페이지")
    public CommonResponse<MemberResponseDTO.myPage> getMyPage(
            @AuthenticationPrincipal CustomUserDetails user
    ){
        return CommonResponse.onSuccess(memberService.getMyPage(user));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    public CommonResponse<MemberResponseDTO.login> login(@RequestBody MemberRequestDTO.login request) {
        return CommonResponse.onSuccess(memberService.login(request));
    }

}
