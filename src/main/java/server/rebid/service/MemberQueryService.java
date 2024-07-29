package server.rebid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Member;
import server.rebid.repository.MemberRepository;

import static server.rebid.dto.response.MemberResponse.IsMemberAddressWrittenDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public String getMemberAddress(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return member.getAddress();
    }

    public IsMemberAddressWrittenDTO isMemberAddressWritten(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return IsMemberAddressWrittenDTO.builder().isAddressWritten(member.getAddress() != null).build();
    }
}
