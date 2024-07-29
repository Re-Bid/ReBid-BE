package server.rebid.mapper;

import org.springframework.stereotype.Component;
import server.rebid.dto.response.MemberResponse;
import server.rebid.dto.response.MemberResponse.MyPageDTO;
import server.rebid.dto.response.MemberResponse.OrderInfo;
import server.rebid.dto.response.MemberResponse.SaleInfo;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.entity.Member;
import server.rebid.entity.enums.BidStatus;

import java.util.List;

@Component
public class MemberMapper {
    public List<OrderInfo> mapMemberOrder(List<BidHistory> dto){
        return dto.stream().map(d ->
                        OrderInfo.builder()
                                .bidId(d.getBid().getId())
                                .itemName(d.getBid().getItemName())
                                .imageUrl(d.getBid().getItemImages().get(0).toString())
                                .bidTime(d.getBid().getBidStatus().equals(BidStatus.COMPLETE_BID) ? d.getBid().getEndDate() : null)  // 입찰 시간
                                .bidPrice(d.getPrice())
                                .bidStatus(d.getBid().getBidStatus().getDescription())
                                .build())
                .toList();
    }

    public List<SaleInfo> mapMemberSale(List<Bid> dto){
        return dto.stream().map(d ->
                SaleInfo.builder()
                        .bidId(d.getId())
                        .itemName(d.getItemName())
                        .imageUrl(d.getItemImages().get(0).toString())
                        .bidTime(d.getBidStatus().equals(BidStatus.COMPLETE_BID) ? d.getEndDate() : null)
                        .bidStatus(d.getBidStatus().getDescription())
                        .build())
                .toList();
    }

    public MyPageDTO toMyPageDTO(Member member, List<OrderInfo> orderInfos, List<SaleInfo> saleInfos){
        return MyPageDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .orders(orderInfos)
                .sales(saleInfos)
                .build();
    }
}
