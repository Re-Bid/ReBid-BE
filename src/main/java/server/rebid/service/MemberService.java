package server.rebid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.auth.CustomUserDetails;
import server.rebid.auth.SecurityUtil;
import server.rebid.auth.jwt.TokenDto;
import server.rebid.dto.request.MemberRequestDTO;
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

    private final BCryptPasswordEncoder encoder;


    @Transactional
    public MemberResponseDTO.signup signup (MemberRequestDTO.signup request) {
        String password = encoder.encode(request.getPassword());
        Member member = MemberMapper.toMember(request, password);
        Member savedMember = memberCommandService.save(member);
        savedMember.setUserId(String.valueOf(savedMember.getId()));
        return MemberMapper.toSignup(savedMember);
    }

    @Transactional
    public MemberResponseDTO.login login(MemberRequestDTO.login request) {
        Member member = memberQueryService.findByEmail(request.getEmail());

        TokenDto loginResponse = memberCommandService.login(member, request.getEmail(), request.getPassword());

        return MemberMapper.toLogin(member, loginResponse.getAccessToken(), loginResponse.getRefreshToken());
    }

    @Transactional(readOnly=true)
    public MemberResponseDTO.myPage getMyPage(CustomUserDetails user) {
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
