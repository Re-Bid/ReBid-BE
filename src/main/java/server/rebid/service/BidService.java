package server.rebid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.auth.CustomUserDetails;
import server.rebid.auth.SecurityUtil;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.AdminBidRequest;
import server.rebid.dto.request.AdminBidRequest.ConfirmRealTimeBid;
import server.rebid.dto.request.BidRequestDTO;
import server.rebid.dto.response.AdminBidResponse.BidForAdminDTO;
import server.rebid.dto.response.AdminBidResponse.BidIdDTO;
import server.rebid.dto.response.AdminBidResponse.GetBidsByStatusDTO;
import server.rebid.dto.response.BidHistoryResponseDTO;
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

    @Transactional
    public BidResponseDTO.addBid addBid(CustomUserDetails user, BidRequestDTO.addBid request) {
        Category category = categoryQueryService.findByName(request.getCategory());
        Member member = memberQueryService.findById(user.getMemberId());
        Bid bid = BidMapper.toBid(member, request, category);

        List<ItemImage> itemImages = BidMapper.toItemImages(request);
        itemImages.forEach(image -> image.setBid(bid));

        itemImageCommandService.saveItemImages(itemImages);
        Bid savedBid = bidCommandService.addBid(bid);
        return BidMapper.toAddBid(savedBid);
    }

    @Transactional(readOnly = true)
    public BidResponseDTO.getBids getBids() {
        List<Bid> bids = bidQueryService.findAll();
        return BidMapper.toGetBids(bids);
    }

    @Transactional(readOnly = true)
    public BidResponseDTO.getBidDetails getBidDetails(CustomUserDetails user, Long bidId) {
        Member member = memberQueryService.findById(user.getMemberId());
        Bid bid = bidQueryService.findById(bidId);
        List<String> imageUrls = bid.getItemImages().stream()
                .map(ItemImage::getImageUrl)
                .collect(Collectors.toList());
        boolean isHeart = heartQueryService.existsByMemberAndBid(member, bid);
        return BidMapper.toGetBidDetails(bid, imageUrls, isHeart);
    }


    @Transactional(readOnly = true)
    public BidResponseDTO.getBids getImminentBids() {
        List<Bid> bids = bidQueryService.getImminentBids();
        return BidMapper.toGetBids(bids);
    }

    @Transactional(readOnly = true)
    public BidResponseDTO.getBids getBidsByCategory(String categoryName) {
        List<Bid> bids = bidQueryService.getBidsByCategory(categoryName);
        return BidMapper.toGetBids(bids);
    }

    @Transactional(readOnly = true)
    public getRejectReason getRejectReason(CustomUserDetails user, Long bidId) {
        Bid bid = bidQueryService.findById(bidId);
        String rejectReason = bid.getCancelReason();
        return BidMapper.toGetRejectReason(rejectReason);
    }

    @Transactional
    public BidResponseDTO.addBidHistory addBidHistory(CustomUserDetails user, Long bidId, BidRequestDTO.addBidHistory request) {
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
    @Transactional(readOnly = true)
    public GetBidsByStatusDTO getBidsByStatus(CustomUserDetails user, Integer page, Integer size, String status) {
        Member member = memberQueryService.findById(user.getMemberId());
        MemberRole role = member.getRole();
        List<Bid> bids = bidQueryService.getBidsByStatus(page, size, status, role);
        return BidMapper.toGetBidsByStatusDTO(bids);
    }

    /**
     * 상품 승인 반려하기
     */
    @Transactional
    public BidIdDTO rejectBid(CustomUserDetails user, Long bidId, String rejectReason) {
        Member member = memberQueryService.findById(user.getMemberId());
        MemberRole role = member.getRole();
        Long modifyBidId = bidCommandService.rejectBid(bidId, role, rejectReason);
        return BidMapper.toBidIdDTO(bidId);
    }

    /**
     * (실시간) 상품 승인 하기
     */
    @Transactional
    public BidIdDTO confirmRealTimeBid(CustomUserDetails user, Long bidId, ConfirmRealTimeBid requestDTO) {
        Member member = memberQueryService.findById(user.getMemberId());
        MemberRole role = member.getRole();
        Long modifyBidId = bidCommandService.confirmRealTime(bidId, role, requestDTO);
        return BidMapper.toBidIdDTO(bidId);
    }

    /**
     * (기간 경매) 상품 승인 하기
     */
    @Transactional
    public BidIdDTO confirmReservationBid(CustomUserDetails user, Long bidId, AdminBidRequest.ConfirmReservationBid requestDTO) {
        Member member = memberQueryService.findById(user.getMemberId());
        MemberRole role = member.getRole();
        Long modifyBidId = bidCommandService.confirmReservationBid(bidId, role, requestDTO);
        return BidMapper.toBidIdDTO(bidId);
    }

    /**
     * 관리자 제품 상세 화면
     */
    @Transactional(readOnly = true)
    public BidForAdminDTO getBidForAdmin(CustomUserDetails user, Long bidId) {
        Member member = memberQueryService.findById(user.getMemberId());
        MemberRole role = member.getRole();
        Bid bid = bidQueryService.getBidForAdmin(bidId, role);
        return BidMapper.toBidForAdminDTO(bid);
    }

    @Transactional
    public BidResponseDTO.addHeart addHeart(CustomUserDetails user, Long bidId) {
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

    public BidResponseDTO.getBidDetails getBidDetailsWithOutUser(Long bidId) {
        Bid bid = bidQueryService.findById(bidId);
        List<String> imageUrls = bid.getItemImages().stream()
                .map(ItemImage::getImageUrl)
                .collect(Collectors.toList());

        return BidMapper.toGetBidDetails(bid, imageUrls, false);
    }

    public BidHistoryResponseDTO.getBidHistories getBidHistories(Long bidId) {
        Bid bid = bidQueryService.findById(bidId);
        List<BidHistory> bidHistories = bid.getBidHistories();
        return BidMapper.toGetBidHistories(bidHistories);
    }

    public BidResponseDTO.getMemberHeart getMemberHeart(CustomUserDetails user) {
        Member member = memberQueryService.findById(user.getMemberId());
        List<Heart> hearts = memberQueryService.getMemberHeart(member);
        return BidMapper.toGetMemberHeart(hearts);
    }

    public BidResponseDTO.getBids getCategoryRecommend(Long categoryId) {
        List<Bid> bids = bidQueryService.getCategoryRecommend(categoryId);
        return BidMapper.toGetBids(bids);
    }

    public BidResponseDTO.getBids getPersonalRecommend(Long memberId) {
        List<Bid> bids = bidQueryService.getPersonalRecommend(memberId);
        return BidMapper.toGetBids(bids);
    }

    public void requestBidLearning(){
        bidCommandService.requestBidLearning();
    }
}
