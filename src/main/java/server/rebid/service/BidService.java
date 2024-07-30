package server.rebid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.rebid.dto.request.AdminBidRequest;
import server.rebid.dto.request.AdminBidRequest.ConfirmRealTimeBid;
import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.AdminBidResponse.BidForAdminDTO;
import server.rebid.dto.response.AdminBidResponse.BidIdDTO;
import server.rebid.dto.response.AdminBidResponse.GetBidsByStatusDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.dto.response.BidResponseDTO.getRejectReason;
import server.rebid.entity.Bid;
import server.rebid.entity.Category;
import server.rebid.entity.enums.MemberRole;
import server.rebid.mapper.BidMapper;
import server.rebid.service.command.BidCommandService;
import server.rebid.service.query.BidQueryService;
import server.rebid.service.query.CategoryQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
@Slf4j
public class BidService {

    private final BidCommandService bidCommandService;
    private final BidQueryService bidQueryService;

    private final CategoryQueryService categoryQueryService;

    public BidResponseDTO.addBid addBid(BidRequestDTO.addBid request) {
        Category category = categoryQueryService.findByName(request.getCategory());
        Bid bid = BidMapper.toBid(request, category);
        Bid savedBid = bidCommandService.addBid(bid);
        return BidMapper.toAddBid(savedBid);
    }

    public BidResponseDTO.getBids getBids() {
        List<Bid> bids = bidQueryService.findAll();
        return BidMapper.toGetBids(bids);
    }

    public BidResponseDTO.getBidDetails getBidDetails(Long bidId) {
        Bid bid = bidQueryService.findById(bidId);
        String imageUrl = bid.getItemImages().get(0).getImageUrl();
        return BidMapper.toGetBidDetails(bid, imageUrl);
    }

    public BidResponseDTO.getBids getRealTimeBids() {
        List<Bid> bids = bidQueryService.getRealTimeBids();
        return BidMapper.toGetBids(bids);
    }

    public BidResponseDTO.getBids getImminentBids() {
        List<Bid> bids = bidQueryService.getImminentBids();
        return BidMapper.toGetBids(bids);
    }


    public BidResponseDTO.getBids getBidsByCategory(String categoryName) {
        List<Bid> bids = bidQueryService.getBidsByCategory(categoryName);
        return BidMapper.toGetBids(bids);
    }

    public getRejectReason getRejectReason(Long bidId) {
        Bid bid = bidQueryService.findById(bidId);
        String rejectReason = bid.getCancelReason();
        return BidMapper.toGetRejectReason(rejectReason);
    }


    /**
     * 승인 대기 중인 제품 조회
     */
    public GetBidsByStatusDTO getBidsByStatus(Integer page, Integer size, String status, MemberRole role) {
        List<Bid> bids = bidQueryService.getBidsByStatus(page, size, status, role);
        return BidMapper.toGetBidsByStatusDTO(bids);
    }

    /**
     * 상품 승인 반려하기
     */
    public BidIdDTO rejectBid(Long bidId, MemberRole role, String rejestReason) {
        Long modifyBidId = bidCommandService.rejectBid(bidId, role, rejestReason);
        return BidMapper.toBidIdDTO(bidId);
    }

    /**
     * (실시간) 상품 승인 하기
     */
    public BidIdDTO confirmRealTimeBid(Long bidId, MemberRole role, ConfirmRealTimeBid requestDTO) {
        Long modifyBidId = bidCommandService.confirmRealTime(bidId, role, requestDTO);
        return BidMapper.toBidIdDTO(bidId);
    }

    /**
     * (기간 경매) 상품 승인 하기
     */
    public BidIdDTO confirmReservationBid(Long bidId, MemberRole role, AdminBidRequest.ConfirmReservationBid requestDTO) {
        Long modifyBidId = bidCommandService.confirmReservationBid(bidId, role, requestDTO);
        return BidMapper.toBidIdDTO(bidId);
    }

    /**
     * 관리자 제품 상세 화면
     */
    public BidForAdminDTO getBidForAdmin(Long bidId, MemberRole role) {
        Bid bid = bidQueryService.getBidForAdmin(bidId, role);
        return BidMapper.toBidForAdminDTO(bid);
    }


}
