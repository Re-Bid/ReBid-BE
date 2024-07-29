package server.rebid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.MemberRequest;
import server.rebid.dto.response.MemberResponse;
import server.rebid.entity.Member;
import server.rebid.repository.MemberRepository;

import static server.rebid.dto.request.MemberRequest.*;
import static server.rebid.dto.response.MemberResponse.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;

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


}
