package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.auth.jwt.TokenDto;
import server.rebid.auth.jwt.TokenProvider;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Member;
import server.rebid.entity.enums.MemberRole;
import server.rebid.repository.MemberRepository;

import java.util.List;

import static server.rebid.dto.response.MemberResponseDTO.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public void setRefreshToken(Long memberId, String refreshToken){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        member.setRefreshToken(refreshToken);
    }

    public boolean isAddressWritten(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return member.getAddress() != null;
    }

    public MemberIdDTO modifyAddress(Long memberId, String address){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        member.modifyAddress(address);
        return MemberIdDTO.builder()
                .memberId(member.getId())
                .build();
    }


    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public TokenDto login(Member member, String email, String password) {

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(member, List.of(new SimpleGrantedAuthority(MemberRole.ROLE_USER.name())));

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
    }
}
