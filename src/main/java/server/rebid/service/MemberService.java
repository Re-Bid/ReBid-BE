package server.rebid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.dto.response.MemberResponseDTO;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.entity.Member;
import server.rebid.mapper.MemberMapper;
import server.rebid.service.command.MemberCommandService;
import server.rebid.service.query.BidHistoryQueryService;
import server.rebid.service.query.BidQueryService;
import server.rebid.service.query.MemberQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final BidQueryService bidQueryService;
    private final BidHistoryQueryService bidHistoryQueryService;


    @Transactional(readOnly=true)
    public MemberResponseDTO.myPage getMyPage(CustomOAuth2User user) {
//        Long memberId = SecurityUtil.getCurrentMemberId();
        Long memberId = user.getMemberId();
        Member member = memberQueryService.findById(memberId);
        // 주문 제품 확인
        List<BidHistory> orders = bidHistoryQueryService.getMemberOrders(memberId);
        // 판매 제품 확인
        List<Bid> sales = bidQueryService.getMemberSales(memberId);
        // 전체 매핑
        List<MemberResponseDTO.OrderInfo> orderInfos = MemberMapper.mapMemberOrder(orders);
        List<MemberResponseDTO.SaleInfo> saleInfos = MemberMapper.mapMemberSale(sales);

        return MemberMapper.toMyPage(member, orderInfos, saleInfos);
    }
}
