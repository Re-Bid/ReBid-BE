package server.rebid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.entity.Member;
import server.rebid.repository.BidHistoryRepository;
import server.rebid.repository.BidRepository;
import server.rebid.repository.MemberRepository;

import java.util.List;

import static server.rebid.dto.response.MemberResponse.IsMemberAddressWrittenDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final BidRepository bidRepository;

    public String getMemberAddress(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return member.getAddress();
    }

    public IsMemberAddressWrittenDTO isMemberAddressWritten(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        return IsMemberAddressWrittenDTO.builder().isAddressWritten(member.getAddress() != null).build();
    }

    // TODO
    public void getMyPage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        // 주문 제품 확인
        //List<BidHistory> orders = bidHistoryRepository.getMemberOrders(memberId);
        // 판매 제품 확인
        //List<Bid> sales =
        // 전체 매핑
    }
}
