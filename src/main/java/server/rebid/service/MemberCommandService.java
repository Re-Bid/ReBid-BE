package server.rebid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Member;
import server.rebid.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;

    public void setRefreshToken(Long memberId, String refreshToken){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        member.setRefreshToken(refreshToken);
    }



}
