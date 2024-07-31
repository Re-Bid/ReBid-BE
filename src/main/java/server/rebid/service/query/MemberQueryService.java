package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.response.MemberResponseDTO.OrderInfo;
import server.rebid.dto.response.MemberResponseDTO.SaleInfo;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.entity.Heart;
import server.rebid.entity.Member;
import server.rebid.mapper.MemberMapper;
import server.rebid.repository.*;

import java.util.List;
import java.util.Optional;

import static server.rebid.dto.response.MemberResponseDTO.IsMemberAddressWrittenDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final BidRepository bidRepository;
    private final MemberMapper memberMapper;
    private final HeartRepository heartRepository;
    private final HeartQueryRepository heartQueryRepository;

    public Boolean isMemberExist(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
    }

    public String getMemberAddress(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return member.getAddress();
    }

    public IsMemberAddressWrittenDTO isMemberAddressWritten(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return IsMemberAddressWrittenDTO.builder().isAddressWritten(member.getAddress() != null).build();
    }

    public boolean isMemberAddressWrittenV2(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return member.getAddress() != null;
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
    }

    public List<Heart> getMemberHeart(Member member) {
        return heartQueryRepository.getMemberHeart(member);
    }
}
