package server.rebid.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import server.rebid.dto.request.MemberRequestDTO;
import server.rebid.dto.response.MemberResponseDTO;
import server.rebid.dto.response.MemberResponseDTO.OrderInfo;
import server.rebid.dto.response.MemberResponseDTO.SaleInfo;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.entity.Member;
import server.rebid.entity.enums.BidStatus;
import server.rebid.entity.enums.MemberRole;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    public static MemberResponseDTO.signup toSignup(Member savedMember) {
        return MemberResponseDTO.signup.builder()
                .memberId(savedMember.getId())
                .build();
    }

    public static MemberResponseDTO.login toLogin(Member member, String accessToken, String refreshToken) {
        return MemberResponseDTO.login.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static List<OrderInfo> mapMemberOrder(List<BidHistory> dto){
        return dto.stream().map(d ->
                        OrderInfo.builder()
                                .bidId(d.getBid().getId())
                                .itemName(d.getBid().getItemName())
                                .imageUrl(d.getBid().getItemImages().get(0).getImageUrl())
                                .bidTime(d.getBid().getBidStatus().equals(BidStatus.COMPLETE_BID) ? d.getBid().getEndDate() : null)  // 입찰 시간
                                .bidPrice(d.getPrice())
                                .bidStatus(d.getBid().getBidStatus()!=null? d.getBid().getBidStatus().getDescription(): null)
                                .build())
                .toList();
    }

    public static List<SaleInfo> mapMemberSale(List<Bid> dto){
        return dto.stream().map(d ->
                SaleInfo.builder()
                        .bidId(d.getId())
                        .itemName(d.getItemName())
                        .imageUrl(d.getItemImages().get(0).getImageUrl())
                        .bidTime(d.getBidStatus().equals(BidStatus.COMPLETE_BID) ? d.getEndDate() : null)
                        .bidStatus(d.getBidStatus()!=null? d.getBidStatus().getDescription(): null)
                        .build())
                .toList();
    }

    public static MemberResponseDTO.myPage toMyPage(Member member, List<OrderInfo> orderInfos, List<SaleInfo> saleInfos){
        return MemberResponseDTO.myPage.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .orders(orderInfos)
                .sales(saleInfos)
                .build();
    }

    public static Member toMember(MemberRequestDTO.signup request, String password){
        return Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(password)
                .isAllowed(true)
                .role(MemberRole.ROLE_USER)
                .build();
    }
}
