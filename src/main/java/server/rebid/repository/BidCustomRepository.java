package server.rebid.repository;

import server.rebid.entity.Bid;

import java.util.List;

public interface BidCustomRepository {

    List<Bid> getMemberSales(Long memberId);
}
