package server.rebid.repository;

import server.rebid.entity.Bid;
import server.rebid.entity.enums.ConfirmStatus;

import java.util.List;

public interface BidCustomRepository {

    List<Bid> getMemberSales(Long memberId);

    List<Bid> getBidsByStatus(Integer page, Integer size, ConfirmStatus status);
}
