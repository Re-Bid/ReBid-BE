package server.rebid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.AdminBidRequest;
import server.rebid.dto.request.AdminBidRequest.ConfirmRealTimeBid;
import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.AdminBidResponse.BidForAdminDTO;
import server.rebid.dto.response.AdminBidResponse.BidIdDTO;
import server.rebid.dto.response.AdminBidResponse.GetBidsByStatusDTO;
import server.rebid.dto.response.BidResponseDTO;
import server.rebid.dto.response.BidResponseDTO.getRejectReason;
import server.rebid.entity.*;
import server.rebid.entity.enums.MemberRole;
import server.rebid.mapper.BidMapper;
import server.rebid.mapper.HeartMapper;
import server.rebid.service.command.BidCommandService;
import server.rebid.service.command.BidHistoryCommandService;
import server.rebid.service.command.HeartCommandService;
import server.rebid.service.command.ItemImageCommandService;
import server.rebid.service.query.BidQueryService;
import server.rebid.service.query.CategoryQueryService;
import server.rebid.service.query.HeartQueryService;
import server.rebid.service.query.MemberQueryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional
@Slf4j
public class BidService {

    private final BidCommandService bidCommandService;
    private final BidQueryService bidQueryService;

    private final BidHistoryCommandService bidHistoryCommandService;

    private final MemberQueryService memberQueryService;

    private final ItemImageCommandService itemImageCommandService;

    private final CategoryQueryService categoryQueryService;

    private final HeartCommandService heartCommandService;
    private final HeartQueryService heartQueryService;

    public BidResponseDTO.addBid addBid(CustomOAuth2User user, BidRequestDTO.addBid request) {
        Category category = categoryQueryService.findByName(request.getCategory());
        Member member = memberQueryService.findById(user.getMemberId());
        Bid bid = BidMapper.toBid(member, request, category);

        List<ItemImage> itemImages = BidMapper.toItemImages(request);
        itemImages.forEach(image -> image.setBid(bid));

        itemImageCommandService.saveItemImages(itemImages);
        Bid savedBid = bidCommandService.addBid(bid);
        return BidMapper.toAddBid(savedBid);
    }

    public BidResponseDTO.getBids getBids() {
        List<Bid> bids = bidQueryService.findAll();
        return BidMapper.toGetBids(bids);
    }

    public BidResponseDTO.getBidDetails getBidDetails(CustomOAuth2User user, Long bidId) {
        Member member = memberQueryService.findById(user.getMemberId());
        Bid bid = bidQueryService.findById(bidId);
        List<String> imageUrls = bid.getItemImages().stream()
                .map(ItemImage::getImageUrl)
                .collect(Collectors.toList());
        boolean isHeart = heartQueryService.existsByMemberAndBid(member, bid);
        return BidMapper.toGetBidDetails(bid, imageUrls, isHeart);
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

    public BidResponseDTO.addBidHistory addBidHistory(CustomOAuth2User user, Long bidId, BidRequestDTO.addBidHistory request) {
        Member member = memberQueryService.findById(user.getMemberId());
        Bid bid = bidQueryService.findById(bidId);

        if (bid.getMember().equals(member)) {
            throw new GeneralException(GlobalErrorCode.WRONG_BID_ITEM);
        }

//        if (bid.getStartingPrice() >= request.getPrice()) {
//            throw new GeneralException(GlobalErrorCode.INVALID_BID_HISTORY);
//        }

        BidHistory bidHistory = BidMapper.toBidHistory(member, bid, request);
        BidHistory savedBidHistory = bidHistoryCommandService.addBidHistory(bidHistory);
        return BidMapper.toAddBidHistory(savedBidHistory);
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
    public BidIdDTO rejectBid(Long bidId, MemberRole role, String rejectReason) {
        Long modifyBidId = bidCommandService.rejectBid(bidId, role, rejectReason);
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


    public BidResponseDTO.addHeart addHeart(CustomOAuth2User user, Long bidId) {
        Member member = memberQueryService.findById(user.getMemberId());
        Bid bid = bidQueryService.findById(bidId);

        if (heartQueryService.existsByMemberAndBid(member, bid)) {
            heartCommandService.deleteHeart(member, bid);
            return HeartMapper.toAddHeart(bid, false);
        }

        Heart heart = HeartMapper.toHeart(member, bid);
        Heart savedHeart = heartCommandService.addHeart(heart);
        return HeartMapper.toAddHeart(bid, true);
    }

    public BidResponseDTO.getBidDetails getBidDetailsWithoutUser(Long bidId) {
        Bid bid = bidQueryService.findById(bidId);
        List<String> imageUrls = bid.getItemImages().stream()
                .map(ItemImage::getImageUrl)
                .collect(Collectors.toList());
        return BidMapper.toGetBidDetails(bid, imageUrls, false);
    }
}
